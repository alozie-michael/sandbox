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

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final ApiCallLogsStatus DEFAULT_STATUS = ApiCallLogsStatus.SUCESS;
    private static final ApiCallLogsStatus UPDATED_STATUS = ApiCallLogsStatus.FAILED;

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
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .status(DEFAULT_STATUS);
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
        assertThat(testApiCallLogs.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApiCallLogs.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testApiCallLogs.getStatus()).isEqualTo(DEFAULT_STATUS);
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
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = apiCallLogsRepository.findAll().size();
        // set the field null
        apiCallLogs.setName(null);

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
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = apiCallLogsRepository.findAll().size();
        // set the field null
        apiCallLogs.setCode(null);

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
    public void getAllApiCallLogs() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList
        restApiCallLogsMockMvc.perform(get("/api/api-call-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiCallLogs.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
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
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where name equals to DEFAULT_NAME
        defaultApiCallLogsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the apiCallLogsList where name equals to UPDATED_NAME
        defaultApiCallLogsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultApiCallLogsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the apiCallLogsList where name equals to UPDATED_NAME
        defaultApiCallLogsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where name is not null
        defaultApiCallLogsShouldBeFound("name.specified=true");

        // Get all the apiCallLogsList where name is null
        defaultApiCallLogsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where code equals to DEFAULT_CODE
        defaultApiCallLogsShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the apiCallLogsList where code equals to UPDATED_CODE
        defaultApiCallLogsShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where code in DEFAULT_CODE or UPDATED_CODE
        defaultApiCallLogsShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the apiCallLogsList where code equals to UPDATED_CODE
        defaultApiCallLogsShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllApiCallLogsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        apiCallLogsRepository.saveAndFlush(apiCallLogs);

        // Get all the apiCallLogsList where code is not null
        defaultApiCallLogsShouldBeFound("code.specified=true");

        // Get all the apiCallLogsList where code is null
        defaultApiCallLogsShouldNotBeFound("code.specified=false");
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
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultApiCallLogsShouldBeFound(String filter) throws Exception {
        restApiCallLogsMockMvc.perform(get("/api/api-call-logs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiCallLogs.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

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
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .status(UPDATED_STATUS);
        ApiCallLogsDTO apiCallLogsDTO = apiCallLogsMapper.toDto(updatedApiCallLogs);

        restApiCallLogsMockMvc.perform(put("/api/api-call-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiCallLogsDTO)))
            .andExpect(status().isOk());

        // Validate the ApiCallLogs in the database
        List<ApiCallLogs> apiCallLogsList = apiCallLogsRepository.findAll();
        assertThat(apiCallLogsList).hasSize(databaseSizeBeforeUpdate);
        ApiCallLogs testApiCallLogs = apiCallLogsList.get(apiCallLogsList.size() - 1);
        assertThat(testApiCallLogs.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApiCallLogs.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testApiCallLogs.getStatus()).isEqualTo(UPDATED_STATUS);
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
