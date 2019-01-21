package com.apifuze.cockpit.web.rest;

import com.apifuze.cockpit.ApicockpitApp;

import com.apifuze.cockpit.domain.ApiCallLogs;
import com.apifuze.cockpit.repository.ApiCallLogsRepository;
import com.apifuze.cockpit.service.ApiCallLogsService;
import com.apifuze.cockpit.service.dto.ApiCallLogsDTO;
import com.apifuze.cockpit.service.mapper.ApiCallLogsMapper;
import com.apifuze.cockpit.web.rest.errors.ExceptionTranslator;
import com.apifuze.cockpit.service.dto.ApiCallLogsCriteria;
import com.apifuze.cockpit.service.ApiCallLogsQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.apifuze.cockpit.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apifuze.cockpit.domain.enumeration.ApiCallLogsStatus;
/**
 * Test class for the ApiCallLogsResource REST controller.
 *
 * @see ApiCallLogsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApicockpitApp.class)
public class ApiCallLogsResourceIntTest {

    private static final ApiCallLogsStatus DEFAULT_STATUS = ApiCallLogsStatus.SUCESS;
    private static final ApiCallLogsStatus UPDATED_STATUS = ApiCallLogsStatus.FAILED;

    private static final String DEFAULT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_API_NAME = "AAAAAAAAAA";
    private static final String UPDATED_API_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE = "BBBBBBBBBB";

    private static final Instant DEFAULT_REQUEST_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUEST_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RESPONSE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESPONSE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ApiCallLogsRepository apiCallLogsRepository;

    @Autowired
    private ApiCallLogsMapper apiCallLogsMapper;

    @Autowired
    private ApiCallLogsService apiCallLogsService;

    @Autowired
    private ApiCallLogsQueryService apiCallLogsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApiCallLogsMockMvc;

    private ApiCallLogs apiCallLogs;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApiCallLogsResource apiCallLogsResource = new ApiCallLogsResource(apiCallLogsService, apiCallLogsQueryService);
        this.restApiCallLogsMockMvc = MockMvcBuilders.standaloneSetup(apiCallLogsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApiCallLogs createEntity(EntityManager em) {
        ApiCallLogs apiCallLogs = new ApiCallLogs()
            .status(DEFAULT_STATUS)
            .projectName(DEFAULT_PROJECT_NAME)
            .apiName(DEFAULT_API_NAME)
            .profile(DEFAULT_PROFILE)
            .requestDate(DEFAULT_REQUEST_DATE)
            .responseDate(DEFAULT_RESPONSE_DATE);
        return apiCallLogs;
    }

    @Before
    public void initTest() {
        apiCallLogs = createEntity(em);
    }

    @Test
    @Transactional
    public void createApiCallLogs() throws Exception {
        int databaseSizeBeforeCreate = apiCallLogsRepository.findAll().size();

        // Create the ApiCallLogs
        ApiCallLogsDTO apiCallLogsDTO = apiCallLogsMapper.toDto(apiCallLogs);
        restApiCallLogsMockMvc.perform(post("/api/api-call-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiCallLogsDTO)))
            .andExpect(status().isCreated());

        // Validate the ApiCallLogs in the database
        List<ApiCallLogs> apiCallLogsList = apiCallLogsRepository.findAll();
        assertThat(apiCallLogsList).hasSize(databaseSizeBeforeCreate + 1);
        ApiCallLogs testApiCallLogs = apiCallLogsList.get(apiCallLogsList.size() - 1);
        assertThat(testApiCallLogs.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testApiCallLogs.getProjectName()).isEqualTo(DEFAULT_PROJECT_NAME);
        assertThat(testApiCallLogs.getApiName()).isEqualTo(DEFAULT_API_NAME);
        assertThat(testApiCallLogs.getProfile()).isEqualTo(DEFAULT_PROFILE);
        assertThat(testApiCallLogs.getRequestDate()).isEqualTo(DEFAULT_REQUEST_DATE);
        assertThat(testApiCallLogs.getResponseDate()).isEqualTo(DEFAULT_RESPONSE_DATE);
    }

    @Test
    @Transactional
    public void createApiCallLogsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = apiCallLogsRepository.findAll().size();

        // Create the ApiCallLogs with an existing ID
        apiCallLogs.setId(1L);
        ApiCallLogsDTO apiCallLogsDTO = apiCallLogsMapper.toDto(apiCallLogs);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApiCallLogsMockMvc.perform(post("/api/api-call-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiCallLogsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApiCallLogs in the database
        List<ApiCallLogs> apiCallLogsList = apiCallLogsRepository.findAll();
        assertThat(apiCallLogsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = apiCallLogsRepository.findAll().size();
        // set the field null
        apiCallLogs.setStatus(null);

        // Create the ApiCallLogs, which fails.
        ApiCallLogsDTO apiCallLogsDTO = apiCallLogsMapper.toDto(apiCallLogs);

        restApiCallLogsMockMvc.perform(post("/api/api-call-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiCallLogsDTO)))
            .andExpect(status().isBadRequest());

        List<ApiCallLogs> apiCallLogsList = apiCallLogsRepository.findAll();
        assertThat(apiCallLogsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProjectNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = apiCallLogsRepository.findAll().size();
        // set the field null
        apiCallLogs.setProjectName(null);

        // Create the ApiCallLogs, which fails.
        ApiCallLogsDTO apiCallLogsDTO = apiCallLogsMapper.toDto(apiCallLogs);

        restApiCallLogsMockMvc.perform(post("/api/api-call-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiCallLogsDTO)))
            .andExpect(status().isBadRequest());

        List<ApiCallLogs> apiCallLogsList = apiCallLogsRepository.findAll();
        assertThat(apiCallLogsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApiNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = apiCallLogsRepository.findAll().size();
        // set the field null
        apiCallLogs.setApiName(null);

        // Create the ApiCallLogs, which fails.
        ApiCallLogsDTO apiCallLogsDTO = apiCallLogsMapper.toDto(apiCallLogs);

        restApiCallLogsMockMvc.perform(post("/api/api-call-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiCallLogsDTO)))
            .andExpect(status().isBadRequest());

        List<ApiCallLogs> apiCallLogsList = apiCallLogsRepository.findAll();
        assertThat(apiCallLogsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProfileIsRequired() throws Exception {
        int databaseSizeBeforeTest = apiCallLogsRepository.findAll().size();
        // set the field null
        apiCallLogs.setProfile(null);

        // Create the ApiCallLogs, which fails.
        ApiCallLogsDTO apiCallLogsDTO = apiCallLogsMapper.toDto(apiCallLogs);

        restApiCallLogsMockMvc.perform(post("/api/api-call-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiCallLogsDTO)))
            .andExpect(status().isBadRequest());

        List<ApiCallLogs> apiCallLogsList = apiCallLogsRepository.findAll();
        assertThat(apiCallLogsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRequestDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = apiCallLogsRepository.findAll().size();
        // set the field null
        apiCallLogs.setRequestDate(null);

        // Create the ApiCallLogs, which fails.
        ApiCallLogsDTO apiCallLogsDTO = apiCallLogsMapper.toDto(apiCallLogs);

        restApiCallLogsMockMvc.perform(post("/api/api-call-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiCallLogsDTO)))
            .andExpect(status().isBadRequest());

        List<ApiCallLogs> apiCallLogsList = apiCallLogsRepository.findAll();
        assertThat(apiCallLogsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResponseDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = apiCallLogsRepository.findAll().size();
        // set the field null
        apiCallLogs.setResponseDate(null);

        // Create the ApiCallLogs, which fails.
        ApiCallLogsDTO apiCallLogsDTO = apiCallLogsMapper.toDto(apiCallLogs);

        restApiCallLogsMockMvc.perform(post("/api/api-call-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiCallLogsDTO)))
            .andExpect(status().isBadRequest());

        List<ApiCallLogs> apiCallLogsList = apiCallLogsRepository.findAll();
        assertThat(apiCallLogsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApiCallLogs() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList
        restApiCallLogsMockMvc.perform(get("/api/api-call-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiCallLogs.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME.toString())))
            .andExpect(jsonPath("$.[*].apiName").value(hasItem(DEFAULT_API_NAME.toString())))
            .andExpect(jsonPath("$.[*].profile").value(hasItem(DEFAULT_PROFILE.toString())))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].responseDate").value(hasItem(DEFAULT_RESPONSE_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getApiCallLogs() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get the apiCallLogs
        restApiCallLogsMockMvc.perform(get("/api/api-call-logs/{id}", apiCallLogs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(apiCallLogs.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.projectName").value(DEFAULT_PROJECT_NAME.toString()))
            .andExpect(jsonPath("$.apiName").value(DEFAULT_API_NAME.toString()))
            .andExpect(jsonPath("$.profile").value(DEFAULT_PROFILE.toString()))
            .andExpect(jsonPath("$.requestDate").value(DEFAULT_REQUEST_DATE.toString()))
            .andExpect(jsonPath("$.responseDate").value(DEFAULT_RESPONSE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where status equals to DEFAULT_STATUS
        defaultApiCallLogsShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the apiCallLogsList where status equals to UPDATED_STATUS
        defaultApiCallLogsShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultApiCallLogsShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the apiCallLogsList where status equals to UPDATED_STATUS
        defaultApiCallLogsShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where status is not null
        defaultApiCallLogsShouldBeFound("status.specified=true");

        // Get all the apiCallLogsList where status is null
        defaultApiCallLogsShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByProjectNameIsEqualToSomething() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where projectName equals to DEFAULT_PROJECT_NAME
        defaultApiCallLogsShouldBeFound("projectName.equals=" + DEFAULT_PROJECT_NAME);

        // Get all the apiCallLogsList where projectName equals to UPDATED_PROJECT_NAME
        defaultApiCallLogsShouldNotBeFound("projectName.equals=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByProjectNameIsInShouldWork() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where projectName in DEFAULT_PROJECT_NAME or UPDATED_PROJECT_NAME
        defaultApiCallLogsShouldBeFound("projectName.in=" + DEFAULT_PROJECT_NAME + "," + UPDATED_PROJECT_NAME);

        // Get all the apiCallLogsList where projectName equals to UPDATED_PROJECT_NAME
        defaultApiCallLogsShouldNotBeFound("projectName.in=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByProjectNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where projectName is not null
        defaultApiCallLogsShouldBeFound("projectName.specified=true");

        // Get all the apiCallLogsList where projectName is null
        defaultApiCallLogsShouldNotBeFound("projectName.specified=false");
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByApiNameIsEqualToSomething() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where apiName equals to DEFAULT_API_NAME
        defaultApiCallLogsShouldBeFound("apiName.equals=" + DEFAULT_API_NAME);

        // Get all the apiCallLogsList where apiName equals to UPDATED_API_NAME
        defaultApiCallLogsShouldNotBeFound("apiName.equals=" + UPDATED_API_NAME);
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByApiNameIsInShouldWork() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where apiName in DEFAULT_API_NAME or UPDATED_API_NAME
        defaultApiCallLogsShouldBeFound("apiName.in=" + DEFAULT_API_NAME + "," + UPDATED_API_NAME);

        // Get all the apiCallLogsList where apiName equals to UPDATED_API_NAME
        defaultApiCallLogsShouldNotBeFound("apiName.in=" + UPDATED_API_NAME);
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByApiNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where apiName is not null
        defaultApiCallLogsShouldBeFound("apiName.specified=true");

        // Get all the apiCallLogsList where apiName is null
        defaultApiCallLogsShouldNotBeFound("apiName.specified=false");
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where profile equals to DEFAULT_PROFILE
        defaultApiCallLogsShouldBeFound("profile.equals=" + DEFAULT_PROFILE);

        // Get all the apiCallLogsList where profile equals to UPDATED_PROFILE
        defaultApiCallLogsShouldNotBeFound("profile.equals=" + UPDATED_PROFILE);
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByProfileIsInShouldWork() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where profile in DEFAULT_PROFILE or UPDATED_PROFILE
        defaultApiCallLogsShouldBeFound("profile.in=" + DEFAULT_PROFILE + "," + UPDATED_PROFILE);

        // Get all the apiCallLogsList where profile equals to UPDATED_PROFILE
        defaultApiCallLogsShouldNotBeFound("profile.in=" + UPDATED_PROFILE);
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByProfileIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where profile is not null
        defaultApiCallLogsShouldBeFound("profile.specified=true");

        // Get all the apiCallLogsList where profile is null
        defaultApiCallLogsShouldNotBeFound("profile.specified=false");
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByRequestDateIsEqualToSomething() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where requestDate equals to DEFAULT_REQUEST_DATE
        defaultApiCallLogsShouldBeFound("requestDate.equals=" + DEFAULT_REQUEST_DATE);

        // Get all the apiCallLogsList where requestDate equals to UPDATED_REQUEST_DATE
        defaultApiCallLogsShouldNotBeFound("requestDate.equals=" + UPDATED_REQUEST_DATE);
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByRequestDateIsInShouldWork() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where requestDate in DEFAULT_REQUEST_DATE or UPDATED_REQUEST_DATE
        defaultApiCallLogsShouldBeFound("requestDate.in=" + DEFAULT_REQUEST_DATE + "," + UPDATED_REQUEST_DATE);

        // Get all the apiCallLogsList where requestDate equals to UPDATED_REQUEST_DATE
        defaultApiCallLogsShouldNotBeFound("requestDate.in=" + UPDATED_REQUEST_DATE);
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByRequestDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where requestDate is not null
        defaultApiCallLogsShouldBeFound("requestDate.specified=true");

        // Get all the apiCallLogsList where requestDate is null
        defaultApiCallLogsShouldNotBeFound("requestDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByResponseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where responseDate equals to DEFAULT_RESPONSE_DATE
        defaultApiCallLogsShouldBeFound("responseDate.equals=" + DEFAULT_RESPONSE_DATE);

        // Get all the apiCallLogsList where responseDate equals to UPDATED_RESPONSE_DATE
        defaultApiCallLogsShouldNotBeFound("responseDate.equals=" + UPDATED_RESPONSE_DATE);
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByResponseDateIsInShouldWork() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where responseDate in DEFAULT_RESPONSE_DATE or UPDATED_RESPONSE_DATE
        defaultApiCallLogsShouldBeFound("responseDate.in=" + DEFAULT_RESPONSE_DATE + "," + UPDATED_RESPONSE_DATE);

        // Get all the apiCallLogsList where responseDate equals to UPDATED_RESPONSE_DATE
        defaultApiCallLogsShouldNotBeFound("responseDate.in=" + UPDATED_RESPONSE_DATE);
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByResponseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where responseDate is not null
        defaultApiCallLogsShouldBeFound("responseDate.specified=true");

        // Get all the apiCallLogsList where responseDate is null
        defaultApiCallLogsShouldNotBeFound("responseDate.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultApiCallLogsShouldBeFound(String filter) throws Exception {
        restApiCallLogsMockMvc.perform(get("/api/api-call-logs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiCallLogs.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME.toString())))
            .andExpect(jsonPath("$.[*].apiName").value(hasItem(DEFAULT_API_NAME.toString())))
            .andExpect(jsonPath("$.[*].profile").value(hasItem(DEFAULT_PROFILE.toString())))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].responseDate").value(hasItem(DEFAULT_RESPONSE_DATE.toString())));

        // Check, that the count call also returns 1
        restApiCallLogsMockMvc.perform(get("/api/api-call-logs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultApiCallLogsShouldNotBeFound(String filter) throws Exception {
        restApiCallLogsMockMvc.perform(get("/api/api-call-logs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApiCallLogsMockMvc.perform(get("/api/api-call-logs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingApiCallLogs() throws Exception {
        // Get the apiCallLogs
        restApiCallLogsMockMvc.perform(get("/api/api-call-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApiCallLogs() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        int databaseSizeBeforeUpdate = apiCallLogsRepository.findAll().size();

        // Update the apiCallLogs
        ApiCallLogs updatedApiCallLogs = apiCallLogsRepository.findById(apiCallLogs.getId()).get();
        // Disconnect from session so that the updates on updatedApiCallLogs are not directly saved in db
        em.detach(updatedApiCallLogs);
        updatedApiCallLogs
            .status(UPDATED_STATUS)
            .projectName(UPDATED_PROJECT_NAME)
            .apiName(UPDATED_API_NAME)
            .profile(UPDATED_PROFILE)
            .requestDate(UPDATED_REQUEST_DATE)
            .responseDate(UPDATED_RESPONSE_DATE);
        ApiCallLogsDTO apiCallLogsDTO = apiCallLogsMapper.toDto(updatedApiCallLogs);

        restApiCallLogsMockMvc.perform(put("/api/api-call-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiCallLogsDTO)))
            .andExpect(status().isOk());

        // Validate the ApiCallLogs in the database
        List<ApiCallLogs> apiCallLogsList = apiCallLogsRepository.findAll();
        assertThat(apiCallLogsList).hasSize(databaseSizeBeforeUpdate);
        ApiCallLogs testApiCallLogs = apiCallLogsList.get(apiCallLogsList.size() - 1);
        assertThat(testApiCallLogs.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testApiCallLogs.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testApiCallLogs.getApiName()).isEqualTo(UPDATED_API_NAME);
        assertThat(testApiCallLogs.getProfile()).isEqualTo(UPDATED_PROFILE);
        assertThat(testApiCallLogs.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testApiCallLogs.getResponseDate()).isEqualTo(UPDATED_RESPONSE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingApiCallLogs() throws Exception {
        int databaseSizeBeforeUpdate = apiCallLogsRepository.findAll().size();

        // Create the ApiCallLogs
        ApiCallLogsDTO apiCallLogsDTO = apiCallLogsMapper.toDto(apiCallLogs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiCallLogsMockMvc.perform(put("/api/api-call-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiCallLogsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApiCallLogs in the database
        List<ApiCallLogs> apiCallLogsList = apiCallLogsRepository.findAll();
        assertThat(apiCallLogsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApiCallLogs() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        int databaseSizeBeforeDelete = apiCallLogsRepository.findAll().size();

        // Get the apiCallLogs
        restApiCallLogsMockMvc.perform(delete("/api/api-call-logs/{id}", apiCallLogs.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ApiCallLogs> apiCallLogsList = apiCallLogsRepository.findAll();
        assertThat(apiCallLogsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApiCallLogs.class);
        ApiCallLogs apiCallLogs1 = new ApiCallLogs();
        apiCallLogs1.setId(1L);
        ApiCallLogs apiCallLogs2 = new ApiCallLogs();
        apiCallLogs2.setId(apiCallLogs1.getId());
        assertThat(apiCallLogs1).isEqualTo(apiCallLogs2);
        apiCallLogs2.setId(2L);
        assertThat(apiCallLogs1).isNotEqualTo(apiCallLogs2);
        apiCallLogs1.setId(null);
        assertThat(apiCallLogs1).isNotEqualTo(apiCallLogs2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApiCallLogsDTO.class);
        ApiCallLogsDTO apiCallLogsDTO1 = new ApiCallLogsDTO();
        apiCallLogsDTO1.setId(1L);
        ApiCallLogsDTO apiCallLogsDTO2 = new ApiCallLogsDTO();
        assertThat(apiCallLogsDTO1).isNotEqualTo(apiCallLogsDTO2);
        apiCallLogsDTO2.setId(apiCallLogsDTO1.getId());
        assertThat(apiCallLogsDTO1).isEqualTo(apiCallLogsDTO2);
        apiCallLogsDTO2.setId(2L);
        assertThat(apiCallLogsDTO1).isNotEqualTo(apiCallLogsDTO2);
        apiCallLogsDTO1.setId(null);
        assertThat(apiCallLogsDTO1).isNotEqualTo(apiCallLogsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(apiCallLogsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(apiCallLogsMapper.fromId(null)).isNull();
    }
}
