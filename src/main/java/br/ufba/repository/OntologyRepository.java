package br.ufba.repository;

import br.ufba.domain.Ontology;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ontology entity.
 */
@SuppressWarnings("unused")
public interface OntologyRepository extends JpaRepository<Ontology,Long> {

}
