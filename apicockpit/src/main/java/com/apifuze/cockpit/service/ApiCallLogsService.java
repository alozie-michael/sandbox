package com.apifuze.cockpit.service;

import com.apifuze.cockpit.domain.ApiCallLogs;
import com.apifuze.cockpit.repository.ApiCallLogsRepository;
import com.apifuze.cockpit.service.dto.ApiCallLogsDTO;
import com.apifuze.cockpit.service.mapper.ApiCallLogsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing ApiCallLogs.
 */
@Service
@Transactional
public class ApiCallLogsService {

    private final Logger log = LoggerFactory.getLogger(ApiCallLogsService.class);

    private final ApiCallLogsRepository apiCallLogsRepository;

    private final ApiCallLogsMapper apiCallLogsMapper;

    public ApiCallLogsService(ApiCallLogsRepository apiCallLogsRepository, ApiCallLogsMapper apiCallLogsMapper) {
        this.apiCallLogsRepository = apiCallLogsRepository;
        this.apiCallLogsMapper = apiCallLogsMapper;
    }

    /**
     * Save a apiCallLogs.
     *
     * @param apiCallLogsDTO the entity to save
     * @return the persisted entity
     */
    public ApiCallLogsDTO save(ApiCallLogsDTO apiCallLogsDTO) {
        log.debug("Request to save ApiCallLogs : {}", apiCallLogsDTO);

        ApiCallLogs apiCallLogs = apiCallLogsMapper.toEntity(apiCallLogsDTO);
        apiCallLogs = apiCallLogsRepository.save(apiCallLogs);
        return apiCallLogsMapper.toDto(apiCallLogs);
    }

    /**
     * Get all the apiCallLogs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ApiCallLogsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ApiCallLogs");
        return apiCallLogsRepository.findAll(pageable)
            .map(apiCallLogsMapper::toDto);
    }


    /**
     * Get one apiCallLogs by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ApiCallLogsDTO> findOne(Long id) {
        log.debug("Request to get ApiCallLogs : {}", id);
        return apiCallLogsRepository.findById(id)
            .map(apiCallLogsMapper::toDto);
    }

    /**
     * Delete the apiCallLogs by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ApiCallLogs : {}", id);
        apiCallLogsRepository.deleteById(id);
    }
}
