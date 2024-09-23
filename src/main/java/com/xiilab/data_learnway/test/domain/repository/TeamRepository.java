package com.xiilab.data_learnway.test.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xiilab.data_learnway.test.domain.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

	@Override
	@Query(value = "select t from Team t where t.useStatus = 'U' and t.id = :teamId")
	Optional<Team> findById(Long teamId);
}
