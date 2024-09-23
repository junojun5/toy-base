package com.xiilab.data_learnway.auth.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.xiilab.data_learnway.auth.application.dto.request.LoginRequest;
import com.xiilab.data_learnway.auth.application.dto.request.SignupRequest;
import com.xiilab.data_learnway.auth.application.dto.response.LoginResponse;
import com.xiilab.data_learnway.auth.domain.enums.AuthProvider;
import com.xiilab.data_learnway.common.annotation.ControllerTest;
import com.xiilab.data_learnway.global.context.WithUser;
import com.xiilab.data_learnway.user.domain.User;
import com.xiilab.data_learnway.user.domain.repository.UserRepository;

@SpringBootTest
class AuthServiceTest extends ControllerTest {
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AuthService authService;

	@Test
	void login() {
		// given
		User saveUser = User.builder()
			.email("testId@naver.com")
			.name("test")
			.password(passwordEncoder.encode("pass"))
			.authProvider(AuthProvider.valueOf("basic"))
			.build();
		userRepository.save(saveUser);

		LoginRequest loginRequest = new LoginRequest(saveUser.getEmail(), "pass");

		// when
		LoginResponse loginResponse = authService.login(loginRequest);

		// then
		assertThat(loginResponse.getUserName()).isEqualTo("test");
		assertThat(loginResponse.getUserEmail()).isEqualTo("testId@naver.com");
	}

	@Test
	void signup() {
		SignupRequest signupRequest = new SignupRequest("juno8469@naver.com", "test", "", "");

		authService.signup(signupRequest);

		Optional<User> findUser = userRepository.findByEmail(signupRequest.getEmail());

		assertThat(findUser.isPresent()).isTrue();
		assertThat(findUser.get().getEmail()).isEqualTo("juno8469@naver.com");
		assertThat(findUser.get().getName()).isEqualTo("test");

	}

	@Test
	@WithUser(email = "testId@naver.com")
	void logout() {
		authService.logout();
		Optional<User> findUser = userRepository.findByEmail("testId@naver.com");

		assertThat(findUser.isPresent()).isTrue();
		assertThat(findUser.get().getRefreshToken()).isNull();
	}
}
