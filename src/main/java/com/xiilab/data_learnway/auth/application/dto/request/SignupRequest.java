package com.xiilab.data_learnway.auth.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignupRequest {
	@NotBlank(message = "이메일 주소가 공백일 수 없습니다.")
	private String email;
	@NotBlank(message = "사용자명이 공백일 수 없습니다.")
	private String userName;
	@NotBlank(message = "비밀번호가 공백일 수 없습니다.")
	private String password;
	@NotBlank(message = "비밀번호 재확인이 공백일 수 없습니다.")
	private String passwordConfirm;
}
