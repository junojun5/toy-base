package com.xiilab.data_learnway.auth.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiilab.data_learnway.auth.application.dto.request.LoginRequest;
import com.xiilab.data_learnway.auth.application.dto.request.SignupRequest;
import com.xiilab.data_learnway.auth.application.dto.response.LoginResponse;
import com.xiilab.data_learnway.auth.application.protocols.jwt.utils.JwtUtils;
import com.xiilab.data_learnway.auth.application.protocols.user.UserDetailsImpl;
import com.xiilab.data_learnway.auth.domain.enums.AuthProvider;
import com.xiilab.data_learnway.auth.exception.AuthErrorCode;
import com.xiilab.data_learnway.global.exception.RestApiException;
import com.xiilab.data_learnway.infrastructure.mail.event.SignupSuccessEvent;
import com.xiilab.data_learnway.user.domain.User;
import com.xiilab.data_learnway.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtUtils;
	private final ApplicationEventPublisher eventPublisher;

	public LoginResponse login(LoginRequest loginRequest) {
		Authentication authentication = null;

		try {
			// 사용자 요청 정보로 UsernamePasswordAuthenticationToken 만들어서 인증 및 인가처리
			authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		} catch (BadCredentialsException e) {
			log.error("BadCredentialsException : {}", e);
			throw new RestApiException(AuthErrorCode.LOGIN_FAIL_MESSAGE);
		}

		// 사용자 정보를 SecurityContextHolder에 저장
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();

		// 리프레쉬 토큰 발급 후 DB에 저장
		generateAndSaveRefreshTokenByUserId(userDetails.getId());

		return new LoginResponse(userDetails.getId(), userDetails.getName(), userDetails.getEmail());
	}

	public void signup(SignupRequest signupRequest) {
		// 비밀번호, 비밀번호 재입력 확인
		if (!signupRequest.getPassword().equals(signupRequest.getPasswordConfirm())) {
			throw new RestApiException(AuthErrorCode.PASSWORD_MISSMATCH_MESSAGE);
		}

		// 이메일 중복 검사
		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			throw new RestApiException(AuthErrorCode.DUPLICATE_EMAIL_MESSAGE);
		}

		User user = User.builder()
			.email(signupRequest.getEmail())
			.name(signupRequest.getUserName())
			.password(passwordEncoder.encode(signupRequest.getPassword()))
			.authProvider(AuthProvider.basic)
			.build();

		userRepository.save(user);
		eventPublisher.publishEvent(new SignupSuccessEvent(user));

	}

	public void logout() {
		Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!principle.toString().equals("anonymousUser")) {
			// Token에서 ID 추출
			Long userId = ((UserDetailsImpl)principle).getId();
			userRepository.findById(userId).ifPresent(user -> user.updateRefreshToken(null));
		}
	}

	/**
	 * 리프레쉬 토큰 발급 후 저장
	 */
	private void generateAndSaveRefreshTokenByUserId(Long userId) {
		// 리프레쉬 토큰 발급
		//        String refreshToken = jwtUtils.generateRefreshTokenFromUserId(userDetails.getId());
		// 리프레쉬 토큰 DB저장
		User findUser = userRepository.findById(userId)
			.orElseThrow(() -> new RestApiException(AuthErrorCode.LOGIN_FAIL_MESSAGE));
		findUser.updateRefreshToken(jwtUtils.generateRefreshTokenFromUserId(userId));
	}
}
