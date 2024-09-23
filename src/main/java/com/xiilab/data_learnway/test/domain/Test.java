package com.xiilab.data_learnway.test.domain;

import com.xiilab.data_learnway.global.common.BaseEntity;
import com.xiilab.data_learnway.test.domain.dto.TestDto.RequestInsertTest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_TEST")
//@EqualsAndHashCode(exclude = {"team"})
public class Test extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TEST_ID")
	private Long id;

	@Column(name = "TEST_NAME")
	private String testName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEAM_ID")
	private Team team;

	@Builder
	public Test(Long id, String testName, Team team) {
		this.id = id;
		this.testName = testName;
		this.team = team;
	}

	public static Test dtoToEntity(RequestInsertTest request) {
		return Test.builder()
			.testName(request.getTestName())
			.build();
	}

	public void updateTestName(String testName) {
		this.testName = testName;
	}

	public void changeTeam(Team team) {
		if (this.team != null) {
			this.team.getTests().remove(this);
		}
		this.team = team;
		team.getTests().add(this);
	}
}
