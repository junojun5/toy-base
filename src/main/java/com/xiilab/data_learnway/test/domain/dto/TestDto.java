package com.xiilab.data_learnway.test.domain.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.xiilab.data_learnway.test.domain.Test;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

public class TestDto {
	@Getter
	public static class RequestTestDto {
		@NotEmpty
		private String name;
		@Min(1)
		private int age;
	}

	@Getter
	public static class RequestInsertTest {
		@Schema(description = "테스트 이름", example = "testName")
		@NotEmpty
		private String testName;
	}

	@Getter
	public static class RequestUpdateTest {
		@Schema(description = "수정할 테스트 이름", example = "testName")
		@NotEmpty
		private String testName;

		@Schema(description = "수정할 팀 아이디", example = "teamId")
		private Long teamId;
	}

	@Getter
	public static class ResponseTestWithTeam {
		private Long testId;
		private String name;
		private TeamDto team;

		@Builder
		public ResponseTestWithTeam(Long testId, String name, TeamDto team) {
			this.testId = testId;
			this.name = name;
			this.team = team;
		}

		public static ResponseTestWithTeam entityToFindTestWithTeam(Test test) {
			return ResponseTestWithTeam.builder()
				.testId(test.getId())
				.name(test.getTestName())
				.team(TeamDto.teamEntityToTeamDto(test.getTeam()))
				.build();
		}

		public static List<ResponseTestWithTeam> entityListToFindTestWithTeamList(List<Test> tests) {
			return tests.stream()
				.map(ResponseTestWithTeam::entityToFindTestWithTeam)
				.collect(Collectors.toList());
		}
	}

	@Getter
	public static class ResponsePagingTestWithTeam {
		private List<ResponseTestWithTeam> tests;
		private Long totalCount;

		@Builder
		public ResponsePagingTestWithTeam(List<ResponseTestWithTeam> tests, Long totalCount) {
			this.tests = tests;
			this.totalCount = totalCount;
		}
	}

}
