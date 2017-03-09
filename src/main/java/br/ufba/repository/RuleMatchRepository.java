package br.ufba.repository;

import br.ufba.domain.RuleMatch;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RuleMatch entity.
 */
@SuppressWarnings("unused")
public interface RuleMatchRepository extends JpaRepository<RuleMatch,Long> {

}
