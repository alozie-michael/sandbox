package com.apifuze.cockpit.service;

import com.apifuze.cockpit.domain.ApiConsumerProfile;
import com.apifuze.cockpit.domain.ApiProject;
import com.apifuze.cockpit.domain.ApiProjectAuthConfig;
import com.apifuze.cockpit.domain.User;
import com.apifuze.cockpit.repository.*;
import com.apifuze.cockpit.security.AuthoritiesConstants;
import com.apifuze.cockpit.security.SecurityUtils;
import com.apifuze.cockpit.service.dto.ApiProjectDTO;
import com.apifuze.cockpit.service.mapper.ApiProjectMapper;
import com.apifuze.cockpit.service.mapper.ApiProjectServiceMapper;
import com.apifuze.cockpit.service.util.RandomUtil;
import com.apifuze.cockpit.web.rest.errors.ResourceAuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ApiProject.
 */
@Service
@Transactional
public class ApiProjectService {

    private final Logger log = LoggerFactory.getLogger(ApiProjectService.class);

    private final ApiProjectRepository apiProjectRepository;

    private final ApiProjectAuthConfigRepository apiProjectAuthConfigRepository;

    private final UserRepository userRepository;

    private final ApiConsumerProfileRepository apiConsumerProfileRepository;

    private final ApiProjectMapper apiProjectMapper;

    private final ApiProjectServiceMapper apiProjectServiceMapper;

    private final ApiProjectServiceRepository apiProjectServiceRepository;

    public ApiProjectService(ApiProjectRepository apiProjectRepository, ApiProjectAuthConfigRepository apiProjectAuthConfigRepository, UserRepository userRepository,ApiConsumerProfileRepository apiConsumerProfileRepository,ApiProjectServiceRepository apiProjectServiceRepository,ApiProjectMapper apiProjectMapper,ApiProjectServiceMapper apiProjectServiceMapper) {
        this.apiProjectRepository = apiProjectRepository;
        this.apiProjectAuthConfigRepository=apiProjectAuthConfigRepository;
        this.apiProjectMapper = apiProjectMapper;
        this.userRepository=userRepository;
        this.apiConsumerProfileRepository=apiConsumerProfileRepository;
        this.apiProjectServiceRepository=apiProjectServiceRepository;
        this.apiProjectServiceMapper=apiProjectServiceMapper;
    }

    /**
     * Save a apiProject.
     *
     * @param apiProjectDTO the entity to save
     * @return the persisted entity
     */
    public ApiProjectDTO save(ApiProjectDTO apiProjectDTO) throws ResourceAuthorizationException{
        log.debug("Request to save ApiProject : {}", apiProjectDTO);
        Instant created = Instant.now();
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
        }else{
            User user=userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get();
            ApiConsumerProfile consumer = apiConsumerProfileRepository.findByPlatformUserUserId(user.getId());
            apiProjectDTO.setOwnerId(consumer.getId());
        }
        if(apiProjectDTO.getApiKeyId()==null) {
            ApiProjectAuthConfig apiKey = new ApiProjectAuthConfig();
            apiKey.setActive(Boolean.TRUE);
            apiKey.setDateCreated(Instant.now());
            apiKey.setClientSecret(RandomUtil.generatePassword());
            apiKey.setClientId(RandomUtil.generateActivationKey());
            apiKey = apiProjectAuthConfigRepository.save(apiKey);
            apiProjectDTO.setApiKeyId(apiKey.getId());
        }else{

            ApiProjectAuthConfig apiKey;
            Optional<ApiProjectAuthConfig> key = apiProjectAuthConfigRepository.findById(apiProjectDTO.getApiKeyId());
            if(key.isPresent()){
                apiKey=key.get();
                if(apiKey.getProject().getOwner().getId()!=apiProjectDTO.getOwnerId()){
                    throw new ResourceAuthorizationException();
                }
            }
        }
        if(apiProjectDTO.getDateCreated()==null) {
            apiProjectDTO.setDateCreated(Instant.now());
        }
        ApiProject apiProject = apiProjectMapper.toEntity(apiProjectDTO);
        List<com.apifuze.cockpit.domain.ApiProjectService> apis = apiProjectDTO.getApis() .stream().map(s -> {
            com.apifuze.cockpit.domain.ApiProjectService service = apiProjectServiceMapper.toEntity(s);
            service.setActive(Boolean.TRUE);
            service.setDateCreated(created);
            return service;
        }).collect(Collectors.toList());
        apiProject.setApis(new HashSet<>(apiProjectServiceRepository.saveAll(apis)));
        apiProject = apiProjectRepository.save(apiProject);
        return apiProjectMapper.toDto(apiProject);
    }

    /**
     * Get all the apiProjects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ApiProjectDTO> findAll(Pageable pageable) {

        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            log.debug("Request to get all ApiProjects");
            return apiProjectRepository.findAll(pageable).map(apiProjectMapper::toDto);
        }else{
            log.debug("Request to get all ApiProjects for owner");
            User user=userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get();
            ApiConsumerProfile consumer = apiConsumerProfileRepository.findByPlatformUserUserId(user.getId());
            return apiProjectRepository.findAllByOwnerId(consumer.getId(),pageable).map(apiProjectMapper::toDto);
        }
    }

    /**
     * Get all the apiProjects.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ApiProjectDTO> findAll() {
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            log.debug("Request to get all ApiProjects");
            return apiProjectRepository.findAll().stream().map(apiProjectMapper::toDto).collect(Collectors.toList());
        }else{
            log.debug("Request to get all ApiProjects for owner");
            User user=userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get();
            ApiConsumerProfile consumer = apiConsumerProfileRepository.findByPlatformUserUserId(user.getId());
            return apiProjectRepository.findAllByOwnerId(consumer.getId()).stream() .map(apiProjectMapper::toDto).collect(Collectors.toList());
        }
    }

    /**
     * Get all the ApiProject with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<ApiProjectDTO> findAllWithEagerRelationships(Pageable pageable) {
        return apiProjectRepository.findAllWithEagerRelationships(pageable).map(apiProjectMapper::toDto);
    }
    

    /**
     * Get one apiProject by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ApiProjectDTO> findOne(Long id) {
        log.debug("Request to get ApiProject : {}", id);
        return apiProjectRepository.findOneWithEagerRelationships(id)
            .map(apiProjectMapper::toDto);
    }

    /**
     * Delete the apiProject by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ApiProject : {}", id);
        apiProjectRepository.deleteById(id);
    }
}
