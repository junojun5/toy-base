package com.xiilab.data_learnway.test.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TestSearchCondition {

	private String testName;
	private String teamName;

	@Builder
	public TestSearchCondition(String testName, String teamName) {
		this.testName = testName;
		this.teamName = teamName;
	}
}
