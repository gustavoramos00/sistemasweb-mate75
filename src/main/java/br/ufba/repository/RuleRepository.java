package br.ufba.repository;

import br.ufba.domain.Rule;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Rule entity.
 */
@SuppressWarnings("unused")
public interface RuleRepository extends JpaRepository<Rule,Long> {

}
