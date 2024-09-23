package com.xiilab.data_learnway.test.application;

import static com.xiilab.data_learnway.test.domain.dto.TestDto.ResponseTestWithTeam.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiilab.data_learnway.global.exception.RestApiException;
import com.xiilab.data_learnway.test.domain.Team;
import com.xiilab.data_learnway.test.domain.Test;
import com.xiilab.data_learnway.test.domain.dto.TeamDto;
import com.xiilab.data_learnway.test.domain.dto.TestDto.RequestInsertTest;
import com.xiilab.data_learnway.test.domain.dto.TestDto.RequestUpdateTest;
import com.xiilab.data_learnway.test.domain.dto.TestDto.ResponsePagingTestWithTeam;
import com.xiilab.data_learnway.test.domain.dto.TestDto.ResponseTestWithTeam;
import com.xiilab.data_learnway.test.domain.dto.TestSearchCondition;
import com.xiilab.data_learnway.test.domain.repository.TeamRepository;
import com.xiilab.data_learnway.test.domain.repository.TestRepository;
import com.xiilab.data_learnway.test.exception.TestErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TestService {

	private final TestRepository testRepository;
	private final TeamRepository teamRepository;

	public Long insertTest(RequestInsertTest request) {
		Test test = Test.dtoToEntity(request);
		Test saveTest = testRepository.save(test);
		return saveTest.getId();
	}

	public ResponseTestWithTeam getTestById(Long testId) {
		Optional<Test> findTest = testRepository.findById(testId);
		Test test = findTest.orElseThrow(() -> new RestApiException(TestErrorCode.TEST_NOT_FOUND));

		return ResponseTestWithTeam.builder()
			.testId(test.getId())
			.name(test.getTestName())
			.team(TeamDto.teamEntityToTeamDto(test.getTeam()))
			.build();
	}

	public ResponsePagingTestWithTeam getTestList(Pageable pageable, TestSearchCondition condition) {
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

		//        Page<Test> testPage = testRepository.getTests(pageRequest);
		Page<Test> tests = testRepository.getTests(pageRequest, condition);
		// Page<ResponseTestWithTeam> results = tests.map(ResponseTestWithTeam::entityToFindTestWithTeam);
		List<ResponseTestWithTeam> results = entityListToFindTestWithTeamList(tests.getContent());

		return ResponsePagingTestWithTeam.builder()
			.tests(results)
			.totalCount(tests.getTotalElements())
			.build();
	}

	public Long updateTest(RequestUpdateTest request, Long testId) {
		Test test = testRepository.findById(testId)
			.orElseThrow(() -> new RestApiException(TestErrorCode.UPDATE_TEST_NOT_FOUND));

		if (request.getTeamId() != null && request.getTeamId() != 0) {
			Team team = teamRepository.findById(request.getTeamId())
				.orElseThrow(() -> new RestApiException(TestErrorCode.UPDATE_TEST_FAILED));
			test.changeTeam(team);
		}

		test.updateTestName(request.getTestName());
		return test.getId();
	}

	//실제 디비 로우 삭제
	public Long deleteTest(Long testId) {
		testRepository.findById(testId)
			.orElseThrow(() -> new RestApiException(TestErrorCode.DELETE_TEST_NOT_FOUND));
		testRepository.deleteById(testId);
		return testId;
	}

	//USE_STATUS = D 로 삭제
	public Long deleteTest2(Long testId) {
		Test findTest = testRepository.findById(testId)
			.orElseThrow(() -> new RestApiException(TestErrorCode.DELETE_TEST_NOT_FOUND));
		findTest.setUseStatus("D");
		return testId;
	}
}
