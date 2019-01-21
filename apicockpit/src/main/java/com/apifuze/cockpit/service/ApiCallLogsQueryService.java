package com.apifuze.cockpit.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.apifuze.cockpit.domain.ApiCallLogs;
import com.apifuze.cockpit.domain.*; // for static metamodels
import com.apifuze.cockpit.repository.ApiCallLogsRepository;
import com.apifuze.cockpit.service.dto.ApiCallLogsCriteria;
import com.apifuze.cockpit.service.dto.ApiCallLogsDTO;
import com.apifuze.cockpit.service.mapper.ApiCallLogsMapper;

/**
 * Service for executing complex queries for ApiCallLogs entities in the database.
 * The main input is a {@link ApiCallLogsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ApiCallLogsDTO} or a {@link Page} of {@link ApiCallLogsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ApiCallLogsQueryService extends QueryService<ApiCallLogs> {

    private final Logger log = LoggerFactory.getLogger(ApiCallLogsQueryService.class);

    private final ApiCallLogsRepository apiCallLogsRepository;

    private final ApiCallLogsMapper apiCallLogsMapper;

    public ApiCallLogsQueryService(ApiCallLogsRepository apiCallLogsRepository, ApiCallLogsMapper apiCallLogsMapper) {
        this.apiCallLogsRepository = apiCallLogsRepository;
        this.apiCallLogsMapper = apiCallLogsMapper;
    }

    /**
     * Return a {@link List} of {@link ApiCallLogsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ApiCallLogsDTO> findByCriteria(ApiCallLogsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ApiCallLogs> specification = createSpecification(criteria);
        return apiCallLogsMapper.toDto(apiCallLogsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ApiCallLogsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ApiCallLogsDTO> findByCriteria(ApiCallLogsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ApiCallLogs> specification = createSpecification(criteria);
        return apiCallLogsRepository.findAll(specification, page)
            .map(apiCallLogsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ApiCallLogsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ApiCallLogs> specification = createSpecification(criteria);
        return apiCallLogsRepository.count(specification);
    }

    /**
     * Function to convert ApiCallLogsCriteria to a {@link Specification}
     */
    private Specification<ApiCallLogs> createSpecification(ApiCallLogsCriteria criteria) {
        Specification<ApiCallLogs> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ApiCallLogs_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ApiCallLogs_.status));
            }
            if (criteria.getProjectName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProjectName(), ApiCallLogs_.projectName));
            }
            if (criteria.getApiName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApiName(), ApiCallLogs_.apiName));
            }
            if (criteria.getProfile() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProfile(), ApiCallLogs_.profile));
            }
            if (criteria.getRequestDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRequestDate(), ApiCallLogs_.requestDate));
            }
            if (criteria.getResponseDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getResponseDate(), ApiCallLogs_.responseDate));
            }
        }
        return specification;
    }
}
