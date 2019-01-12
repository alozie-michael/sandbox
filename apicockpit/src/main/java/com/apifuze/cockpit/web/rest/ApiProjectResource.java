package com.apifuze.cockpit.web.rest;

import com.apifuze.cockpit.domain.ApiConsumerProfile;
import com.apifuze.cockpit.domain.User;
import com.apifuze.cockpit.repository.ApiConsumerProfileRepository;
import com.apifuze.cockpit.repository.UserRepository;
import com.apifuze.cockpit.security.AuthoritiesConstants;
import com.apifuze.cockpit.security.SecurityUtils;
import com.apifuze.cockpit.service.ApiProjectService;
import com.apifuze.cockpit.service.dto.ApiProjectDTO;
import com.apifuze.cockpit.web.rest.errors.BadRequestAlertException;
import com.apifuze.cockpit.web.rest.util.HeaderUtil;
import com.apifuze.cockpit.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ApiProject.
 */
@RestController
@RequestMapping("/api")
public class ApiProjectResource {

    private final Logger log = LoggerFactory.getLogger(ApiProjectResource.class);

    private static final String ENTITY_NAME = "apiProject";

    private final ApiProjectService apiProjectService;

    private final UserRepository userRepository;

    private final ApiConsumerProfileRepository apiConsumerProfileRepository;



    public ApiProjectResource(ApiProjectService apiProjectService,UserRepository userRepository,ApiConsumerProfileRepository apiConsumerProfileRepository) {
        this.apiProjectService = apiProjectService;
        this.userRepository=userRepository;
        this.apiConsumerProfileRepository=apiConsumerProfileRepository;
    }

    /**
     * POST  /api-projects : Create a new apiProject.
     *
     * @param apiProjectDTO the apiProjectDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new apiProjectDTO, or with status 400 (Bad Request) if the apiProject has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api-projects")
    @Timed
    public ResponseEntity<ApiProjectDTO> createApiProject(@Valid @RequestBody ApiProjectDTO apiProjectDTO) throws URISyntaxException {
        log.debug("REST request to save ApiProject : {}", apiProjectDTO);
        if (apiProjectDTO.getId() != null) {
            throw new BadRequestAlertException("A new Project cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApiProjectDTO result = apiProjectService.save(apiProjectDTO);
        return ResponseEntity.created(new URI("/api/api-projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getName()))
            .body(result);
    }

    /**
     * PUT  /api-projects : Updates an existing apiProject.
     *
     * @param apiProjectDTO the apiProjectDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated apiProjectDTO,
     * or with status 400 (Bad Request) if the apiProjectDTO is not valid,
     * or with status 500 (Internal Server Error) if the apiProjectDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/api-projects")
    @Timed
    public ResponseEntity<ApiProjectDTO> updateApiProject(@Valid @RequestBody ApiProjectDTO apiProjectDTO) throws URISyntaxException {
        log.debug("REST request to update ApiProject : {}", apiProjectDTO);
        if (apiProjectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ApiProjectDTO result = apiProjectService.save(apiProjectDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getName()))
            .body(result);
    }

    /**
     * GET  /api-projects : get all the apiProjects.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of apiProjects in body
     */
    @GetMapping("/api-projects")
    @Timed
    public ResponseEntity<List<ApiProjectDTO>> getAllApiProjects(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of ApiProjects");
        Page<ApiProjectDTO> page;
        if (eagerload) {
            page = apiProjectService.findAllWithEagerRelationships(pageable);
        } else {
            page = apiProjectService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/api-projects?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /api-projects/:id : get the "id" apiProject.
     *
     * @param id the id of the apiProjectDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the apiProjectDTO, or with status 404 (Not Found)
     */
    @GetMapping("/api-projects/{id}")
    @Timed
    public ResponseEntity<ApiProjectDTO> getApiProject(@PathVariable Long id) {
        log.debug("REST request to get ApiProject : {}", id);
        Optional<ApiProjectDTO> apiProjectDTO = apiProjectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(apiProjectDTO);
    }

    /**
     * DELETE  /api-projects/:id : delete the "id" apiProject.
     *
     * @param id the id of the apiProjectDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api-projects/{id}")
    @Timed
    public ResponseEntity<Void> deleteApiProject(@PathVariable Long id) {
        log.debug("REST request to delete ApiProject : {}", id);
        boolean canDelete=false;
        Optional<ApiProjectDTO> apiProjectDTO = apiProjectService.findOne(id);
        if(apiProjectDTO.isPresent()){
            if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
                canDelete=true;
            }else{
                User user=userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get();
                ApiConsumerProfile consumer = apiConsumerProfileRepository.findByPlatformUserUserId(user.getId());

                if(apiProjectDTO.get().getOwnerId()==consumer.getId() && !apiProjectDTO.get().isActive()){
                    canDelete=true;
                }
            }
            if(canDelete){
                apiProjectService.delete(id);
                return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, apiProjectDTO.get().getName())).build();
            }else{
                return ResponseEntity.badRequest().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, apiProjectDTO.get().getName())).build();
            }

        }else{
            return ResponseUtil.wrapOrNotFound(null);
        }

    }
}
