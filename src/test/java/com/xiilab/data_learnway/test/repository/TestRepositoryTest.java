package com.xiilab.data_learnway.test.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.xiilab.data_learnway.common.annotation.RepositoryTest;
import com.xiilab.data_learnway.test.domain.Team;
import com.xiilab.data_learnway.test.domain.dto.TestSearchCondition;
import com.xiilab.data_learnway.test.domain.repository.TeamRepository;
import com.xiilab.data_learnway.test.domain.repository.TestRepository;
import com.xiilab.data_learnway.test.domain.repository.TestRepositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

class TestRepositoryTest extends RepositoryTest {
	@Autowired
	TestRepository testRepository;
	@Autowired
	TestRepositoryImpl testQueryRepository;
	@Autowired
	TeamRepository teamRepository;
	@PersistenceContext
	EntityManager em;

	@BeforeEach
	public void cleanup() {
		testRepository.deleteAll();
		teamRepository.deleteAll();
	}

	@Test
	public void saveTest() {
		com.xiilab.data_learnway.test.domain.Test test = com.xiilab.data_learnway.test.domain.Test.builder()
			.testName("ddd")
			.team(null)
			.build();
		com.xiilab.data_learnway.test.domain.Test savedTestEntity = testRepository.save(test);

		Assertions.assertThat(savedTestEntity.getId()).isEqualTo(1L);
		Assertions.assertThat(savedTestEntity.getTestName()).isEqualTo("ddd");
	}

	@Test
	public void getTestWithTeam() {
		Team team = Team.builder()
			.teamName("team123")
			.build();
		teamRepository.save(team);
		com.xiilab.data_learnway.test.domain.Test test = com.xiilab.data_learnway.test.domain.Test.builder()
			.testName("test123")
			.team(team)
			.build();
		testRepository.save(test);
		em.clear();

		com.xiilab.data_learnway.test.domain.Test findTest = testRepository.findById(1L).get();

		Assertions.assertThat(findTest.getTestName()).isEqualTo("test123");
		Assertions.assertThat(findTest.getTeam().getTeamName()).isEqualTo("team123");
	}

	@Test
	void getTests() {
		Team team = Team.builder()
			.teamName("t1")
			.build();
		teamRepository.save(team);
		com.xiilab.data_learnway.test.domain.Test test1 = com.xiilab.data_learnway.test.domain.Test.builder()
			.testName("test")
			.team(team)
			.build();
		testRepository.save(test1);
		com.xiilab.data_learnway.test.domain.Test test2 = com.xiilab.data_learnway.test.domain.Test.builder()
			.testName("kkk")
			.team(null)
			.build();
		testRepository.save(test2);
		em.clear();

		PageRequest pageRequest = PageRequest.of(0, 10);
		TestSearchCondition condition = TestSearchCondition.builder()
			//            .teamName("t")
			.build();
		Page<com.xiilab.data_learnway.test.domain.Test> tests = testQueryRepository.getTests(
			pageRequest, condition);

		Assertions.assertThat(tests.getContent().size()).isEqualTo(2);
	}

}
