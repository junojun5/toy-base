package com.xiilab.data_learnway.auth.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

	@NotBlank(message = "{auth.notEmpty.userEmail}")
	private String email;

	@NotBlank(message = "{auth.notEmpty.password}")
	private String password;
}
