package br.ufba.web.rest;

import br.ufba.SistemaswebappApp;

import br.ufba.domain.RuleMatch;
import br.ufba.repository.RuleMatchRepository;
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

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RuleMatchResource REST controller.
 *
 * @see RuleMatchResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SistemaswebappApp.class)
public class RuleMatchResourceIntTest {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Autowired
    private RuleMatchRepository ruleMatchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRuleMatchMockMvc;

    private RuleMatch ruleMatch;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            RuleMatchResource ruleMatchResource = new RuleMatchResource(ruleMatchRepository);
        this.restRuleMatchMockMvc = MockMvcBuilders.standaloneSetup(ruleMatchResource)
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
    public static RuleMatch createEntity(EntityManager em) {
        RuleMatch ruleMatch = new RuleMatch()
                .content(DEFAULT_CONTENT);
        return ruleMatch;
    }

    @Before
    public void initTest() {
        ruleMatch = createEntity(em);
    }

    @Test
    @Transactional
    public void createRuleMatch() throws Exception {
        int databaseSizeBeforeCreate = ruleMatchRepository.findAll().size();

        // Create the RuleMatch

        restRuleMatchMockMvc.perform(post("/api/rule-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleMatch)))
            .andExpect(status().isCreated());

        // Validate the RuleMatch in the database
        List<RuleMatch> ruleMatchList = ruleMatchRepository.findAll();
        assertThat(ruleMatchList).hasSize(databaseSizeBeforeCreate + 1);
        RuleMatch testRuleMatch = ruleMatchList.get(ruleMatchList.size() - 1);
        assertThat(testRuleMatch.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createRuleMatchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ruleMatchRepository.findAll().size();

        // Create the RuleMatch with an existing ID
        RuleMatch existingRuleMatch = new RuleMatch();
        existingRuleMatch.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRuleMatchMockMvc.perform(post("/api/rule-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingRuleMatch)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RuleMatch> ruleMatchList = ruleMatchRepository.findAll();
        assertThat(ruleMatchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRuleMatches() throws Exception {
        // Initialize the database
        ruleMatchRepository.saveAndFlush(ruleMatch);

        // Get all the ruleMatchList
        restRuleMatchMockMvc.perform(get("/api/rule-matches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ruleMatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getRuleMatch() throws Exception {
        // Initialize the database
        ruleMatchRepository.saveAndFlush(ruleMatch);

        // Get the ruleMatch
        restRuleMatchMockMvc.perform(get("/api/rule-matches/{id}", ruleMatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ruleMatch.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRuleMatch() throws Exception {
        // Get the ruleMatch
        restRuleMatchMockMvc.perform(get("/api/rule-matches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRuleMatch() throws Exception {
        // Initialize the database
        ruleMatchRepository.saveAndFlush(ruleMatch);
        int databaseSizeBeforeUpdate = ruleMatchRepository.findAll().size();

        // Update the ruleMatch
        RuleMatch updatedRuleMatch = ruleMatchRepository.findOne(ruleMatch.getId());
        updatedRuleMatch
                .content(UPDATED_CONTENT);

        restRuleMatchMockMvc.perform(put("/api/rule-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRuleMatch)))
            .andExpect(status().isOk());

        // Validate the RuleMatch in the database
        List<RuleMatch> ruleMatchList = ruleMatchRepository.findAll();
        assertThat(ruleMatchList).hasSize(databaseSizeBeforeUpdate);
        RuleMatch testRuleMatch = ruleMatchList.get(ruleMatchList.size() - 1);
        assertThat(testRuleMatch.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void updateNonExistingRuleMatch() throws Exception {
        int databaseSizeBeforeUpdate = ruleMatchRepository.findAll().size();

        // Create the RuleMatch

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRuleMatchMockMvc.perform(put("/api/rule-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleMatch)))
            .andExpect(status().isCreated());

        // Validate the RuleMatch in the database
        List<RuleMatch> ruleMatchList = ruleMatchRepository.findAll();
        assertThat(ruleMatchList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRuleMatch() throws Exception {
        // Initialize the database
        ruleMatchRepository.saveAndFlush(ruleMatch);
        int databaseSizeBeforeDelete = ruleMatchRepository.findAll().size();

        // Get the ruleMatch
        restRuleMatchMockMvc.perform(delete("/api/rule-matches/{id}", ruleMatch.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RuleMatch> ruleMatchList = ruleMatchRepository.findAll();
        assertThat(ruleMatchList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RuleMatch.class);
    }
}
