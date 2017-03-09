package br.ufba.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.ufba.domain.RuleMatch;

import br.ufba.repository.RuleMatchRepository;
import br.ufba.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RuleMatch.
 */
@RestController
@RequestMapping("/api")
public class RuleMatchResource {

    private final Logger log = LoggerFactory.getLogger(RuleMatchResource.class);

    private static final String ENTITY_NAME = "ruleMatch";
        
    private final RuleMatchRepository ruleMatchRepository;

    public RuleMatchResource(RuleMatchRepository ruleMatchRepository) {
        this.ruleMatchRepository = ruleMatchRepository;
    }

    /**
     * POST  /rule-matches : Create a new ruleMatch.
     *
     * @param ruleMatch the ruleMatch to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ruleMatch, or with status 400 (Bad Request) if the ruleMatch has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rule-matches")
    @Timed
    public ResponseEntity<RuleMatch> createRuleMatch(@RequestBody RuleMatch ruleMatch) throws URISyntaxException {
        log.debug("REST request to save RuleMatch : {}", ruleMatch);
        if (ruleMatch.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ruleMatch cannot already have an ID")).body(null);
        }
        RuleMatch result = ruleMatchRepository.save(ruleMatch);
        return ResponseEntity.created(new URI("/api/rule-matches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rule-matches : Updates an existing ruleMatch.
     *
     * @param ruleMatch the ruleMatch to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ruleMatch,
     * or with status 400 (Bad Request) if the ruleMatch is not valid,
     * or with status 500 (Internal Server Error) if the ruleMatch couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rule-matches")
    @Timed
    public ResponseEntity<RuleMatch> updateRuleMatch(@RequestBody RuleMatch ruleMatch) throws URISyntaxException {
        log.debug("REST request to update RuleMatch : {}", ruleMatch);
        if (ruleMatch.getId() == null) {
            return createRuleMatch(ruleMatch);
        }
        RuleMatch result = ruleMatchRepository.save(ruleMatch);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ruleMatch.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rule-matches : get all the ruleMatches.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ruleMatches in body
     */
    @GetMapping("/rule-matches")
    @Timed
    public List<RuleMatch> getAllRuleMatches() {
        log.debug("REST request to get all RuleMatches");
        List<RuleMatch> ruleMatches = ruleMatchRepository.findAll();
        return ruleMatches;
    }

    /**
     * GET  /rule-matches/:id : get the "id" ruleMatch.
     *
     * @param id the id of the ruleMatch to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ruleMatch, or with status 404 (Not Found)
     */
    @GetMapping("/rule-matches/{id}")
    @Timed
    public ResponseEntity<RuleMatch> getRuleMatch(@PathVariable Long id) {
        log.debug("REST request to get RuleMatch : {}", id);
        RuleMatch ruleMatch = ruleMatchRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ruleMatch));
    }

    /**
     * DELETE  /rule-matches/:id : delete the "id" ruleMatch.
     *
     * @param id the id of the ruleMatch to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rule-matches/{id}")
    @Timed
    public ResponseEntity<Void> deleteRuleMatch(@PathVariable Long id) {
        log.debug("REST request to delete RuleMatch : {}", id);
        ruleMatchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
