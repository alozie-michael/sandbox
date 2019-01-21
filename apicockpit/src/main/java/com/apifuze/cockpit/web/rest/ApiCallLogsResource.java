package com.apifuze.cockpit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.apifuze.cockpit.service.ApiCallLogsService;
import com.apifuze.cockpit.web.rest.errors.BadRequestAlertException;
import com.apifuze.cockpit.web.rest.util.HeaderUtil;
import com.apifuze.cockpit.web.rest.util.PaginationUtil;
import com.apifuze.cockpit.service.dto.ApiCallLogsDTO;
import com.apifuze.cockpit.service.dto.ApiCallLogsCriteria;
import com.apifuze.cockpit.service.ApiCallLogsQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ApiCallLogs.
 */
@RestController
@RequestMapping("/api")
public class ApiCallLogsResource {

    private final Logger log = LoggerFactory.getLogger(ApiCallLogsResource.class);

    private static final String ENTITY_NAME = "apiCallLogs";

    private final ApiCallLogsService apiCallLogsService;

    private final ApiCallLogsQueryService apiCallLogsQueryService;

    public ApiCallLogsResource(ApiCallLogsService apiCallLogsService, ApiCallLogsQueryService apiCallLogsQueryService) {
        this.apiCallLogsService = apiCallLogsService;
        this.apiCallLogsQueryService = apiCallLogsQueryService;
    }

    /**
     * POST  /api-call-logs : Create a new apiCallLogs.
     *
     * @param apiCallLogsDTO the apiCallLogsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new apiCallLogsDTO, or with status 400 (Bad Request) if the apiCallLogs has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api-call-logs")
    @Timed
    public ResponseEntity<ApiCallLogsDTO> createApiCallLogs(@Valid @RequestBody ApiCallLogsDTO apiCallLogsDTO) throws URISyntaxException {
        log.debug("REST request to save ApiCallLogs : {}", apiCallLogsDTO);
        if (apiCallLogsDTO.getId() != null) {
            throw new BadRequestAlertException("A new apiCallLogs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApiCallLogsDTO result = apiCallLogsService.save(apiCallLogsDTO);
        return ResponseEntity.created(new URI("/api/api-call-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /api-call-logs : Updates an existing apiCallLogs.
     *
     * @param apiCallLogsDTO the apiCallLogsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated apiCallLogsDTO,
     * or with status 400 (Bad Request) if the apiCallLogsDTO is not valid,
     * or with status 500 (Internal Server Error) if the apiCallLogsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/api-call-logs")
    @Timed
    public ResponseEntity<ApiCallLogsDTO> updateApiCallLogs(@Valid @RequestBody ApiCallLogsDTO apiCallLogsDTO) throws URISyntaxException {
        log.debug("REST request to update ApiCallLogs : {}", apiCallLogsDTO);
        if (apiCallLogsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ApiCallLogsDTO result = apiCallLogsService.save(apiCallLogsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, apiCallLogsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /api-call-logs : get all the apiCallLogs.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of apiCallLogs in body
     */
    @GetMapping("/api-call-logs")
    @Timed
    public ResponseEntity<List<ApiCallLogsDTO>> getAllApiCallLogs(ApiCallLogsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ApiCallLogs by criteria: {}", criteria);
        Page<ApiCallLogsDTO> page = apiCallLogsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/api-call-logs");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /api-call-logs/count : count all the apiCallLogs.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/api-call-logs/count")
    @Timed
    public ResponseEntity<Long> countApiCallLogs(ApiCallLogsCriteria criteria) {
        log.debug("REST request to count ApiCallLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(apiCallLogsQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /api-call-logs/:id : get the "id" apiCallLogs.
     *
     * @param id the id of the apiCallLogsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the apiCallLogsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/api-call-logs/{id}")
    @Timed
    public ResponseEntity<ApiCallLogsDTO> getApiCallLogs(@PathVariable Long id) {
        log.debug("REST request to get ApiCallLogs : {}", id);
        Optional<ApiCallLogsDTO> apiCallLogsDTO = apiCallLogsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(apiCallLogsDTO);
    }

    /**
     * DELETE  /api-call-logs/:id : delete the "id" apiCallLogs.
     *
     * @param id the id of the apiCallLogsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api-call-logs/{id}")
    @Timed
    public ResponseEntity<Void> deleteApiCallLogs(@PathVariable Long id) {
        log.debug("REST request to delete ApiCallLogs : {}", id);
        apiCallLogsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
