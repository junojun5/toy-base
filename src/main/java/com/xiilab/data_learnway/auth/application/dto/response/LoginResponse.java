package com.xiilab.data_learnway.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
	private Long id;
	private String userName;
	private String userEmail;
}
