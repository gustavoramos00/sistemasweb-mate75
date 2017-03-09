package br.ufba.service;

import br.ufba.domain.Rule;
import br.ufba.repository.RuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Rule.
 */
@Service
@Transactional
public class RuleService {

    private final Logger log = LoggerFactory.getLogger(RuleService.class);
    
    private final RuleRepository ruleRepository;

    public RuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    /**
     * Save a rule.
     *
     * @param rule the entity to save
     * @return the persisted entity
     */
    public Rule save(Rule rule) {
        log.debug("Request to save Rule : {}", rule);
        Rule result = ruleRepository.save(rule);
        return result;
    }

    /**
     *  Get all the rules.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Rule> findAll() {
        log.debug("Request to get all Rules");
        List<Rule> result = ruleRepository.findAll();

        return result;
    }

    /**
     *  Get one rule by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Rule findOne(Long id) {
        log.debug("Request to get Rule : {}", id);
        Rule rule = ruleRepository.findOne(id);
        return rule;
    }

    /**
     *  Delete the  rule by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Rule : {}", id);
        ruleRepository.delete(id);
    }
}
