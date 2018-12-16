package com.apifuze.cockpit.service;

import com.apifuze.cockpit.domain.ApiConsumerProfile;
import com.apifuze.cockpit.domain.ApiPublisherProfile;
import com.apifuze.cockpit.domain.PlatformUser;
import com.apifuze.cockpit.domain.User;
import com.apifuze.cockpit.repository.ApiConsumerProfileRepository;
import com.apifuze.cockpit.repository.ApiPublisherProfileRepository;
import com.apifuze.cockpit.repository.PlatformUserRepository;
import com.apifuze.cockpit.service.dto.ApiPublisherProfileDTO;
import com.apifuze.cockpit.service.mapper.ApiPublisherProfileMapper;
import com.apifuze.cockpit.web.rest.vm.ManagedUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing ApiPublisherProfile.
 */
@Service
@Transactional
public class ApiPublisherProfileService {

    private final Logger log = LoggerFactory.getLogger(ApiPublisherProfileService.class);

    private final ApiPublisherProfileRepository apiPublisherProfileRepository;

    private final ApiPublisherProfileMapper apiPublisherProfileMapper;

    private final PlatformUserRepository platformUserRepository;
    private final ApiConsumerProfileRepository apiConsumerProfileRepository;

    public ApiPublisherProfileService(ApiPublisherProfileRepository apiPublisherProfileRepository,PlatformUserRepository platformUserRepository,ApiConsumerProfileRepository apiConsumerProfileRepository, ApiPublisherProfileMapper apiPublisherProfileMapper) {
        this.apiPublisherProfileRepository = apiPublisherProfileRepository;
        this.apiPublisherProfileMapper = apiPublisherProfileMapper;
        this.platformUserRepository=platformUserRepository;
        this.apiConsumerProfileRepository=apiConsumerProfileRepository;
    }

    /**
     * Save a apiPublisherProfile.
     *
     * @param apiPublisherProfileDTO the entity to save
     * @return the persisted entity
     */
    public ApiPublisherProfileDTO save(ApiPublisherProfileDTO apiPublisherProfileDTO) {
        log.debug("Request to save ApiPublisherProfile : {}", apiPublisherProfileDTO);

        ApiPublisherProfile apiPublisherProfile = apiPublisherProfileMapper.toEntity(apiPublisherProfileDTO);
        apiPublisherProfile = apiPublisherProfileRepository.save(apiPublisherProfile);
        return apiPublisherProfileMapper.toDto(apiPublisherProfile);
    }

    /**
     * Get all the apiPublisherProfiles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ApiPublisherProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ApiPublisherProfiles");
        return apiPublisherProfileRepository.findAll(pageable)
            .map(apiPublisherProfileMapper::toDto);
    }



    /**
     *  get all the apiPublisherProfiles where PlatformUser is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ApiPublisherProfileDTO> findAllWherePlatformUserIsNull() {
        log.debug("Request to get all apiPublisherProfiles where PlatformUser is null");
        return StreamSupport
            .stream(apiPublisherProfileRepository.findAll().spliterator(), false)
            .filter(apiPublisherProfile -> apiPublisherProfile.getPlatformUser() == null)
            .map(apiPublisherProfileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one apiPublisherProfile by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ApiPublisherProfileDTO> findOne(Long id) {
        log.debug("Request to get ApiPublisherProfile : {}", id);
        return apiPublisherProfileRepository.findById(id)
            .map(apiPublisherProfileMapper::toDto);
    }

    /**
     * Delete the apiPublisherProfile by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ApiPublisherProfile : {}", id);
        apiPublisherProfileRepository.deleteById(id);
    }

    public void resolveUserData(User user, ManagedUserVM managedUserVM) {
        Instant created = Instant.now();
        ApiPublisherProfile publisher = apiPublisherProfileRepository.findAll().get(0);
        PlatformUser platformUser = new PlatformUser();
        platformUser.setPhoneNumber(managedUserVM.getPhoneNumber());
        platformUser.setUser(user);
        platformUser = platformUserRepository.save(platformUser);
        ApiConsumerProfile consumerProfile = new ApiConsumerProfile();
        consumerProfile.setDateCreated(created);
        consumerProfile.setCompany(managedUserVM.getCompany());
        consumerProfile.setActive(Boolean.TRUE);
        consumerProfile.setOwner(publisher);
        consumerProfile.setName(user.getLogin());
        consumerProfile.setPlatformUser(platformUser);
        consumerProfile = apiConsumerProfileRepository.save(consumerProfile);
        platformUser.setApiConsumerProfile(consumerProfile);
        platformUserRepository.save(platformUser);

    }
}
