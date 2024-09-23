package com.xiilab.data_learnway.test.domain.dto;

import com.xiilab.data_learnway.test.domain.Team;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamDto {
	private Long teamId;
	private String teamName;

	public static TeamDto teamEntityToTeamDto(Team team) {
		if (team == null)
			return null;
		return TeamDto.builder()
			.teamId(team.getId())
			.teamName(team.getTeamName())
			.build();
	}
}
