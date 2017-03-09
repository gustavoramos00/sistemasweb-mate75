package br.ufba.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.ufba.domain.Ontology;

import br.ufba.repository.OntologyRepository;
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
 * REST controller for managing Ontology.
 */
@RestController
@RequestMapping("/api")
public class OntologyResource {

    private final Logger log = LoggerFactory.getLogger(OntologyResource.class);

    private static final String ENTITY_NAME = "ontology";
        
    private final OntologyRepository ontologyRepository;

    public OntologyResource(OntologyRepository ontologyRepository) {
        this.ontologyRepository = ontologyRepository;
    }

    /**
     * POST  /ontologies : Create a new ontology.
     *
     * @param ontology the ontology to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ontology, or with status 400 (Bad Request) if the ontology has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ontologies")
    @Timed
    public ResponseEntity<Ontology> createOntology(@RequestBody Ontology ontology) throws URISyntaxException {
        log.debug("REST request to save Ontology : {}", ontology);
        if (ontology.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ontology cannot already have an ID")).body(null);
        }
        Ontology result = ontologyRepository.save(ontology);
        return ResponseEntity.created(new URI("/api/ontologies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ontologies : Updates an existing ontology.
     *
     * @param ontology the ontology to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ontology,
     * or with status 400 (Bad Request) if the ontology is not valid,
     * or with status 500 (Internal Server Error) if the ontology couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ontologies")
    @Timed
    public ResponseEntity<Ontology> updateOntology(@RequestBody Ontology ontology) throws URISyntaxException {
        log.debug("REST request to update Ontology : {}", ontology);
        if (ontology.getId() == null) {
            return createOntology(ontology);
        }
        Ontology result = ontologyRepository.save(ontology);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ontology.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ontologies : get all the ontologies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ontologies in body
     */
    @GetMapping("/ontologies")
    @Timed
    public List<Ontology> getAllOntologies() {
        log.debug("REST request to get all Ontologies");
        List<Ontology> ontologies = ontologyRepository.findAll();
        return ontologies;
    }

    /**
     * GET  /ontologies/:id : get the "id" ontology.
     *
     * @param id the id of the ontology to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ontology, or with status 404 (Not Found)
     */
    @GetMapping("/ontologies/{id}")
    @Timed
    public ResponseEntity<Ontology> getOntology(@PathVariable Long id) {
        log.debug("REST request to get Ontology : {}", id);
        Ontology ontology = ontologyRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ontology));
    }

    /**
     * DELETE  /ontologies/:id : delete the "id" ontology.
     *
     * @param id the id of the ontology to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ontologies/{id}")
    @Timed
    public ResponseEntity<Void> deleteOntology(@PathVariable Long id) {
        log.debug("REST request to delete Ontology : {}", id);
        ontologyRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
