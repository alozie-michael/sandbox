package com.apifuze.cockpit.web.rest;

import com.apifuze.cockpit.ApicockpitApp;
import com.apifuze.cockpit.domain.ApiProject;
import com.apifuze.cockpit.repository.ApiConsumerProfileRepository;
import com.apifuze.cockpit.repository.ApiProjectRepository;
import com.apifuze.cockpit.repository.UserRepository;
import com.apifuze.cockpit.service.ApiProjectService;
import com.apifuze.cockpit.service.dto.ApiProjectDTO;
import com.apifuze.cockpit.service.mapper.ApiProjectMapper;
import com.apifuze.cockpit.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
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
import java.util.ArrayList;
import java.util.List;

import static com.apifuze.cockpit.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ApiProjectResource REST controller.
 *
 * @see ApiProjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApicockpitApp.class)
public class ApiProjectResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ApiProjectRepository apiProjectRepository;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  ApiConsumerProfileRepository apiConsumerProfileRepository;

    @Mock
    private ApiProjectRepository apiProjectRepositoryMock;

    @Autowired
    private ApiProjectMapper apiProjectMapper;

    @Mock
    private ApiProjectService apiProjectServiceMock;

    @Autowired
    private ApiProjectService apiProjectService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApiProjectMockMvc;

    private ApiProject apiProject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApiProjectResource apiProjectResource = new ApiProjectResource(apiProjectService,userRepository,apiConsumerProfileRepository);
        this.restApiProjectMockMvc = MockMvcBuilders.standaloneSetup(apiProjectResource)
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
    public static ApiProject createEntity(EntityManager em) {
        ApiProject apiProject = new ApiProject()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .active(DEFAULT_ACTIVE)
            .dateCreated(DEFAULT_DATE_CREATED);
        return apiProject;
    }

    @Before
    public void initTest() {
        apiProject = createEntity(em);
    }

    @Test
    @Transactional
    public void createApiProject() throws Exception {
        int databaseSizeBeforeCreate = apiProjectRepository.findAll().size();

        // Create the ApiProject
        ApiProjectDTO apiProjectDTO = apiProjectMapper.toDto(apiProject);
        restApiProjectMockMvc.perform(post("/api/api-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiProjectDTO)))
            .andExpect(status().isCreated());

        // Validate the ApiProject in the database
        List<ApiProject> apiProjectList = apiProjectRepository.findAll();
        assertThat(apiProjectList).hasSize(databaseSizeBeforeCreate + 1);
        ApiProject testApiProject = apiProjectList.get(apiProjectList.size() - 1);
        assertThat(testApiProject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApiProject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testApiProject.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testApiProject.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
    }

    @Test
    @Transactional
    public void createApiProjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = apiProjectRepository.findAll().size();

        // Create the ApiProject with an existing ID
        apiProject.setId(1L);
        ApiProjectDTO apiProjectDTO = apiProjectMapper.toDto(apiProject);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApiProjectMockMvc.perform(post("/api/api-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiProjectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApiProject in the database
        List<ApiProject> apiProjectList = apiProjectRepository.findAll();
        assertThat(apiProjectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = apiProjectRepository.findAll().size();
        // set the field null
        apiProject.setName(null);

        // Create the ApiProject, which fails.
        ApiProjectDTO apiProjectDTO = apiProjectMapper.toDto(apiProject);

        restApiProjectMockMvc.perform(post("/api/api-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiProjectDTO)))
            .andExpect(status().isBadRequest());

        List<ApiProject> apiProjectList = apiProjectRepository.findAll();
        assertThat(apiProjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = apiProjectRepository.findAll().size();
        // set the field null
        apiProject.setDateCreated(null);

        // Create the ApiProject, which fails.
        ApiProjectDTO apiProjectDTO = apiProjectMapper.toDto(apiProject);

        restApiProjectMockMvc.perform(post("/api/api-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiProjectDTO)))
            .andExpect(status().isBadRequest());

        List<ApiProject> apiProjectList = apiProjectRepository.findAll();
        assertThat(apiProjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApiProjects() throws Exception {
        // Initialize the database
        apiProjectRepository.saveAndFlush(apiProject);

        // Get all the apiProjectList
        restApiProjectMockMvc.perform(get("/api/api-projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllApiProjectsWithEagerRelationshipsIsEnabled() throws Exception {
        ApiProjectResource apiProjectResource = new ApiProjectResource(apiProjectServiceMock,userRepository,apiConsumerProfileRepository);
        when(apiProjectServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restApiProjectMockMvc = MockMvcBuilders.standaloneSetup(apiProjectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restApiProjectMockMvc.perform(get("/api/api-projects?eagerload=true"))
        .andExpect(status().isOk());

        verify(apiProjectServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllApiProjectsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ApiProjectResource apiProjectResource = new ApiProjectResource(apiProjectServiceMock,userRepository,apiConsumerProfileRepository);
            when(apiProjectServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restApiProjectMockMvc = MockMvcBuilders.standaloneSetup(apiProjectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restApiProjectMockMvc.perform(get("/api/api-projects?eagerload=true"))
        .andExpect(status().isOk());

            verify(apiProjectServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getApiProject() throws Exception {
        // Initialize the database
        apiProjectRepository.saveAndFlush(apiProject);

        // Get the apiProject
        restApiProjectMockMvc.perform(get("/api/api-projects/{id}", apiProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(apiProject.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApiProject() throws Exception {
        // Get the apiProject
        restApiProjectMockMvc.perform(get("/api/api-projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApiProject() throws Exception {
        // Initialize the database
        apiProjectRepository.saveAndFlush(apiProject);

        int databaseSizeBeforeUpdate = apiProjectRepository.findAll().size();

        // Update the apiProject
        ApiProject updatedApiProject = apiProjectRepository.findById(apiProject.getId()).get();
        // Disconnect from session so that the updates on updatedApiProject are not directly saved in db
        em.detach(updatedApiProject);
        updatedApiProject
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE)
            .dateCreated(UPDATED_DATE_CREATED);
        ApiProjectDTO apiProjectDTO = apiProjectMapper.toDto(updatedApiProject);

        restApiProjectMockMvc.perform(put("/api/api-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiProjectDTO)))
            .andExpect(status().isOk());

        // Validate the ApiProject in the database
        List<ApiProject> apiProjectList = apiProjectRepository.findAll();
        assertThat(apiProjectList).hasSize(databaseSizeBeforeUpdate);
        ApiProject testApiProject = apiProjectList.get(apiProjectList.size() - 1);
        assertThat(testApiProject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApiProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApiProject.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testApiProject.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void updateNonExistingApiProject() throws Exception {
        int databaseSizeBeforeUpdate = apiProjectRepository.findAll().size();

        // Create the ApiProject
        ApiProjectDTO apiProjectDTO = apiProjectMapper.toDto(apiProject);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiProjectMockMvc.perform(put("/api/api-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apiProjectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApiProject in the database
        List<ApiProject> apiProjectList = apiProjectRepository.findAll();
        assertThat(apiProjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApiProject() throws Exception {
        // Initialize the database
        apiProjectRepository.saveAndFlush(apiProject);

        int databaseSizeBeforeDelete = apiProjectRepository.findAll().size();

        // Get the apiProject
        restApiProjectMockMvc.perform(delete("/api/api-projects/{id}", apiProject.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ApiProject> apiProjectList = apiProjectRepository.findAll();
        assertThat(apiProjectList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApiProject.class);
        ApiProject apiProject1 = new ApiProject();
        apiProject1.setId(1L);
        ApiProject apiProject2 = new ApiProject();
        apiProject2.setId(apiProject1.getId());
        assertThat(apiProject1).isEqualTo(apiProject2);
        apiProject2.setId(2L);
        assertThat(apiProject1).isNotEqualTo(apiProject2);
        apiProject1.setId(null);
        assertThat(apiProject1).isNotEqualTo(apiProject2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApiProjectDTO.class);
        ApiProjectDTO apiProjectDTO1 = new ApiProjectDTO();
        apiProjectDTO1.setId(1L);
        ApiProjectDTO apiProjectDTO2 = new ApiProjectDTO();
        assertThat(apiProjectDTO1).isNotEqualTo(apiProjectDTO2);
        apiProjectDTO2.setId(apiProjectDTO1.getId());
        assertThat(apiProjectDTO1).isEqualTo(apiProjectDTO2);
        apiProjectDTO2.setId(2L);
        assertThat(apiProjectDTO1).isNotEqualTo(apiProjectDTO2);
        apiProjectDTO1.setId(null);
        assertThat(apiProjectDTO1).isNotEqualTo(apiProjectDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(apiProjectMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(apiProjectMapper.fromId(null)).isNull();
    }
}
