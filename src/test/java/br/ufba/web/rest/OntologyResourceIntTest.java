package br.ufba.web.rest;

import br.ufba.SistemaswebappApp;

import br.ufba.domain.Ontology;
import br.ufba.repository.OntologyRepository;
import br.ufba.web.rest.errors.ExceptionTranslator;

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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OntologyResource REST controller.
 *
 * @see OntologyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SistemaswebappApp.class)
public class OntologyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Autowired
    private OntologyRepository ontologyRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOntologyMockMvc;

    private Ontology ontology;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            OntologyResource ontologyResource = new OntologyResource(ontologyRepository);
        this.restOntologyMockMvc = MockMvcBuilders.standaloneSetup(ontologyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ontology createEntity(EntityManager em) {
        Ontology ontology = new Ontology()
                .name(DEFAULT_NAME)
                .content(DEFAULT_CONTENT);
        return ontology;
    }

    @Before
    public void initTest() {
        ontology = createEntity(em);
    }

    @Test
    @Transactional
    public void createOntology() throws Exception {
        int databaseSizeBeforeCreate = ontologyRepository.findAll().size();

        // Create the Ontology

        restOntologyMockMvc.perform(post("/api/ontologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ontology)))
            .andExpect(status().isCreated());

        // Validate the Ontology in the database
        List<Ontology> ontologyList = ontologyRepository.findAll();
        assertThat(ontologyList).hasSize(databaseSizeBeforeCreate + 1);
        Ontology testOntology = ontologyList.get(ontologyList.size() - 1);
        assertThat(testOntology.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOntology.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createOntologyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ontologyRepository.findAll().size();

        // Create the Ontology with an existing ID
        Ontology existingOntology = new Ontology();
        existingOntology.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOntologyMockMvc.perform(post("/api/ontologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingOntology)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Ontology> ontologyList = ontologyRepository.findAll();
        assertThat(ontologyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOntologies() throws Exception {
        // Initialize the database
        ontologyRepository.saveAndFlush(ontology);

        // Get all the ontologyList
        restOntologyMockMvc.perform(get("/api/ontologies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ontology.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getOntology() throws Exception {
        // Initialize the database
        ontologyRepository.saveAndFlush(ontology);

        // Get the ontology
        restOntologyMockMvc.perform(get("/api/ontologies/{id}", ontology.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ontology.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOntology() throws Exception {
        // Get the ontology
        restOntologyMockMvc.perform(get("/api/ontologies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOntology() throws Exception {
        // Initialize the database
        ontologyRepository.saveAndFlush(ontology);
        int databaseSizeBeforeUpdate = ontologyRepository.findAll().size();

        // Update the ontology
        Ontology updatedOntology = ontologyRepository.findOne(ontology.getId());
        updatedOntology
                .name(UPDATED_NAME)
                .content(UPDATED_CONTENT);

        restOntologyMockMvc.perform(put("/api/ontologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOntology)))
            .andExpect(status().isOk());

        // Validate the Ontology in the database
        List<Ontology> ontologyList = ontologyRepository.findAll();
        assertThat(ontologyList).hasSize(databaseSizeBeforeUpdate);
        Ontology testOntology = ontologyList.get(ontologyList.size() - 1);
        assertThat(testOntology.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOntology.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void updateNonExistingOntology() throws Exception {
        int databaseSizeBeforeUpdate = ontologyRepository.findAll().size();

        // Create the Ontology

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOntologyMockMvc.perform(put("/api/ontologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ontology)))
            .andExpect(status().isCreated());

        // Validate the Ontology in the database
        List<Ontology> ontologyList = ontologyRepository.findAll();
        assertThat(ontologyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOntology() throws Exception {
        // Initialize the database
        ontologyRepository.saveAndFlush(ontology);
        int databaseSizeBeforeDelete = ontologyRepository.findAll().size();

        // Get the ontology
        restOntologyMockMvc.perform(delete("/api/ontologies/{id}", ontology.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ontology> ontologyList = ontologyRepository.findAll();
        assertThat(ontologyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ontology.class);
    }
}
