package com.apifuze.cockpit.web.rest;

import com.apifuze.cockpit.ApicockpitApp;

import com.apifuze.cockpit.domain.ApiProjectService;
import com.apifuze.cockpit.repository.ApiProjectServiceRepository;
import com.apifuze.cockpit.service.ApiProjectServiceService;
import com.apifuze.cockpit.service.ApiServiceConfigService;
import com.apifuze.cockpit.service.dto.ApiProjectServiceDTO;
import com.apifuze.cockpit.service.mapper.ApiProjectServiceMapper;
import com.apifuze.cockpit.web.rest.errors.ExceptionTranslator;

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

/**
 * Test class for the ApiProjectServiceResource REST controller.
 *
 * @see ApiProjectServiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApicockpitApp.class)
public class ApiProjectServiceResourceIntTest {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ApiProjectServiceRepository apiProjectServiceRepository;

    @Autowired
    private ApiProjectServiceMapper apiProjectServiceMapper;

    @Autowired
    private ApiProjectServiceService apiProjectServiceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApiProjectServiceMockMvc;

    private ApiProjectService apiProjectService;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApiServiceConfigService apiServiceConfigService=null;
        final ApiProjectServiceResource apiProjectServiceResource = new ApiProjectServiceResource(apiProjectServiceService, apiServiceConfigService);
        this.restApiProjectServiceMockMvc = MockMvcBuilders.standaloneSetup(apiProjectServiceResource)
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
    public static ApiProjectService createEntity(EntityManager em) {
        ApiProjectService apiProjectService = new ApiProjectService()
            .active(DEFAULT_ACTIVE)
            .name(DEFAULT_NAME)
            .dateCreated(DEFAULT_DATE_CREATED);
        return apiProjectService;
    }

    @Before
    public void initTest() {
        apiProjectService = createEntity(em);
    }

    @Test
    @Transactional
    public void createApiProjectService() throws Exception {
        int databaseSizeBeforeCreate = apiProjectServiceRepository.findAll().size();

        // Create the ApiProjectService
        ApiProjectServiceDTO apiProjectServiceDTO = apiProjectServiceMapper.toDto(apiProjectService);
        restApiProjectServiceMockMvc.perform(post("/api/api-project-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiProjectServiceDTO)))
            .andExpect(status().isCreated());

        // Validate the ApiProjectService in the database
        List<ApiProjectService> apiProjectServiceList = apiProjectServiceRepository.findAll();
        assertThat(apiProjectServiceList).hasSize(databaseSizeBeforeCreate + 1);
        ApiProjectService testApiProjectService = apiProjectServiceList.get(apiProjectServiceList.size() - 1);
        assertThat(testApiProjectService.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testApiProjectService.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApiProjectService.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
    }

    @Test
    @Transactional
    public void createApiProjectServiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = apiProjectServiceRepository.findAll().size();

        // Create the ApiProjectService with an existing ID
        apiProjectService.setId(1L);
        ApiProjectServiceDTO apiProjectServiceDTO = apiProjectServiceMapper.toDto(apiProjectService);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApiProjectServiceMockMvc.perform(post("/api/api-project-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiProjectServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApiProjectService in the database
        List<ApiProjectService> apiProjectServiceList = apiProjectServiceRepository.findAll();
        assertThat(apiProjectServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = apiProjectServiceRepository.findAll().size();
        // set the field null
        apiProjectService.setDateCreated(null);

        // Create the ApiProjectService, which fails.
        ApiProjectServiceDTO apiProjectServiceDTO = apiProjectServiceMapper.toDto(apiProjectService);

        restApiProjectServiceMockMvc.perform(post("/api/api-project-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiProjectServiceDTO)))
            .andExpect(status().isBadRequest());

        List<ApiProjectService> apiProjectServiceList = apiProjectServiceRepository.findAll();
        assertThat(apiProjectServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApiProjectServices() throws Exception {
        // Initialize the database
        apiProjectServiceRepository.saveAndFlush(apiProjectService);

        // Get all the apiProjectServiceList
        restApiProjectServiceMockMvc.perform(get("/api/api-project-services?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiProjectService.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())));
    }
    
    @Test
    @Transactional
    public void getApiProjectService() throws Exception {
        // Initialize the database
        apiProjectServiceRepository.saveAndFlush(apiProjectService);

        // Get the apiProjectService
        restApiProjectServiceMockMvc.perform(get("/api/api-project-services/{id}", apiProjectService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(apiProjectService.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApiProjectService() throws Exception {
        // Get the apiProjectService
        restApiProjectServiceMockMvc.perform(get("/api/api-project-services/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApiProjectService() throws Exception {
        // Initialize the database
        apiProjectServiceRepository.saveAndFlush(apiProjectService);

        int databaseSizeBeforeUpdate = apiProjectServiceRepository.findAll().size();

        // Update the apiProjectService
        ApiProjectService updatedApiProjectService = apiProjectServiceRepository.findById(apiProjectService.getId()).get();
        // Disconnect from session so that the updates on updatedApiProjectService are not directly saved in db
        em.detach(updatedApiProjectService);
        updatedApiProjectService
            .active(UPDATED_ACTIVE)
            .name(UPDATED_NAME)
            .dateCreated(UPDATED_DATE_CREATED);
        ApiProjectServiceDTO apiProjectServiceDTO = apiProjectServiceMapper.toDto(updatedApiProjectService);

        restApiProjectServiceMockMvc.perform(put("/api/api-project-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiProjectServiceDTO)))
            .andExpect(status().isOk());

        // Validate the ApiProjectService in the database
        List<ApiProjectService> apiProjectServiceList = apiProjectServiceRepository.findAll();
        assertThat(apiProjectServiceList).hasSize(databaseSizeBeforeUpdate);
        ApiProjectService testApiProjectService = apiProjectServiceList.get(apiProjectServiceList.size() - 1);
        assertThat(testApiProjectService.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testApiProjectService.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApiProjectService.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void updateNonExistingApiProjectService() throws Exception {
        int databaseSizeBeforeUpdate = apiProjectServiceRepository.findAll().size();

        // Create the ApiProjectService
        ApiProjectServiceDTO apiProjectServiceDTO = apiProjectServiceMapper.toDto(apiProjectService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiProjectServiceMockMvc.perform(put("/api/api-project-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiProjectServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApiProjectService in the database
        List<ApiProjectService> apiProjectServiceList = apiProjectServiceRepository.findAll();
        assertThat(apiProjectServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApiProjectService() throws Exception {
        // Initialize the database
        apiProjectServiceRepository.saveAndFlush(apiProjectService);

        int databaseSizeBeforeDelete = apiProjectServiceRepository.findAll().size();

        // Get the apiProjectService
        restApiProjectServiceMockMvc.perform(delete("/api/api-project-services/{id}", apiProjectService.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ApiProjectService> apiProjectServiceList = apiProjectServiceRepository.findAll();
        assertThat(apiProjectServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApiProjectService.class);
        ApiProjectService apiProjectService1 = new ApiProjectService();
        apiProjectService1.setId(1L);
        ApiProjectService apiProjectService2 = new ApiProjectService();
        apiProjectService2.setId(apiProjectService1.getId());
        assertThat(apiProjectService1).isEqualTo(apiProjectService2);
        apiProjectService2.setId(2L);
        assertThat(apiProjectService1).isNotEqualTo(apiProjectService2);
        apiProjectService1.setId(null);
        assertThat(apiProjectService1).isNotEqualTo(apiProjectService2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApiProjectServiceDTO.class);
        ApiProjectServiceDTO apiProjectServiceDTO1 = new ApiProjectServiceDTO();
        apiProjectServiceDTO1.setId(1L);
        ApiProjectServiceDTO apiProjectServiceDTO2 = new ApiProjectServiceDTO();
        assertThat(apiProjectServiceDTO1).isNotEqualTo(apiProjectServiceDTO2);
        apiProjectServiceDTO2.setId(apiProjectServiceDTO1.getId());
        assertThat(apiProjectServiceDTO1).isEqualTo(apiProjectServiceDTO2);
        apiProjectServiceDTO2.setId(2L);
        assertThat(apiProjectServiceDTO1).isNotEqualTo(apiProjectServiceDTO2);
        apiProjectServiceDTO1.setId(null);
        assertThat(apiProjectServiceDTO1).isNotEqualTo(apiProjectServiceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(apiProjectServiceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(apiProjectServiceMapper.fromId(null)).isNull();
    }
}
