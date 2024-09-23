package com.xiilab.data_learnway.test.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xiilab.data_learnway.test.domain.Test;

@Repository
public interface TestRepository extends JpaRepository<Test, Long>, TestRepositoryCustom {

	@EntityGraph(attributePaths = {"team"})
	@Query(value = "select t from Test t where t.useStatus = 'U'")
	Page<Test> getTests(PageRequest pageRequest);

	@Override
	@Query(value = "select t from Test t where t.useStatus = 'U' and t.id = :id")
	Optional<Test> findById(@Param("id") Long id);

}
