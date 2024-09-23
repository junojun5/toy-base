package com.xiilab.data_learnway.auth.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiilab.data_learnway.auth.application.AuthService;
import com.xiilab.data_learnway.auth.application.dto.request.LoginRequest;
import com.xiilab.data_learnway.auth.application.dto.request.SignupRequest;
import com.xiilab.data_learnway.auth.application.dto.response.LoginResponse;
import com.xiilab.data_learnway.auth.application.protocols.jwt.utils.JwtUtils;
import com.xiilab.data_learnway.global.common.ResponseSingleResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;
	private final JwtUtils jwtUtils;

	@PostMapping("/login")
	public ResponseSingleResult<LoginResponse> login(HttpServletResponse response,
		@Valid @RequestBody LoginRequest loginRequest) {
		LoginResponse loginResponse = authService.login(loginRequest);

		// 쿠키 생성
		jwtUtils.generateJwtCookieByUserId(response, loginResponse.getId());

		ResponseSingleResult<LoginResponse> result = new ResponseSingleResult<>();
		result.setResultCode(HttpStatus.OK.value());
		result.setResultData(loginResponse);

		return result;
	}

	@PostMapping("/signup")
	public ResponseSingleResult<Object> signup(HttpServletRequest request, HttpServletResponse response,
		@Valid @RequestBody SignupRequest signupRequest) {
		authService.signup(signupRequest);
		jwtUtils.getCleanJwtCookie(request, response);

		ResponseSingleResult<Object> responseSingleResult = new ResponseSingleResult<>();
		responseSingleResult.setResultCode(HttpStatus.OK.value());

		return responseSingleResult;
	}

	@GetMapping("/logout")
	public ResponseSingleResult logout(HttpServletRequest request, HttpServletResponse response) {
		// 로그아웃
		authService.logout();

		// 쿠키 초기화
		jwtUtils.getCleanJwtCookie(request, response);

		ResponseSingleResult responseSingleResult = new ResponseSingleResult();
		responseSingleResult.setResultCode(HttpStatus.OK.value());

		return responseSingleResult;
	}
}
