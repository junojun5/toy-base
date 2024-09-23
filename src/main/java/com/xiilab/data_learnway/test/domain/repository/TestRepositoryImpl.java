package com.xiilab.data_learnway.test.domain.repository;

import static com.xiilab.data_learnway.test.domain.QTeam.*;
import static com.xiilab.data_learnway.test.domain.QTest.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xiilab.data_learnway.test.domain.Test;
import com.xiilab.data_learnway.test.domain.dto.TestSearchCondition;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TestRepositoryImpl implements TestRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Test> getTests(PageRequest pageRequest, TestSearchCondition condition) {
		List<Test> tests = queryFactory.selectFrom(test)
			.leftJoin(test.team, team).fetchJoin()
			.where(testNameEq(condition.getTestName()),
				teamNameEq(condition.getTeamName()))
			.offset(pageRequest.getPageNumber())
			.limit(pageRequest.getPageSize())
			.fetch();
		Long count = queryFactory.select(test.count())
			.from(test)
			.leftJoin(test.team, team)
			.where(testNameEq(condition.getTestName()),
				teamNameEq(condition.getTeamName()))
			.fetchOne();
		return new PageImpl<>(tests, pageRequest, count);
	}

	private Predicate testNameEq(String testName) {
		return StringUtils.hasText(testName) ? test.testName.contains(testName) : null;
	}

	private Predicate teamNameEq(String teamName) {
		return StringUtils.hasText(teamName) ? team.teamName.contains(teamName) : null;
	}

}
