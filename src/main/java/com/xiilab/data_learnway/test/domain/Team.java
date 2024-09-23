package com.xiilab.data_learnway.test.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiilab.data_learnway.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_TEAM")
public class Team extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TEAM_ID")
	private Long id;

	@Column(name = "TEAM_NAME")
	private String teamName;

	@JsonIgnore
	@OneToMany(mappedBy = "team")
	private List<Test> tests = new ArrayList<>();

	@Builder
	public Team(Long teamId, String teamName, List<Test> tests) {
		this.id = teamId;
		this.teamName = teamName;
		this.tests = tests;
	}
}
