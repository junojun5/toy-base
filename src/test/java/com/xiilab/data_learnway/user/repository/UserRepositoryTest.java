package com.xiilab.data_learnway.user.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.xiilab.data_learnway.user.domain.User;
import com.xiilab.data_learnway.auth.domain.enums.AuthProvider;
import com.xiilab.data_learnway.user.domain.repository.UserRepository;
import com.xiilab.data_learnway.common.annotation.RepositoryTest;

class UserRepositoryTest extends RepositoryTest {
	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	//    @BeforeEach
	//    public void cleanup() {
	//        userRepository.deleteAll();
	//    }

	@Test
	@DisplayName("이메일로 사용자 찾기")
	void findByEmail() {
		// given
		saveUser("j.seo@xiilab.com", "서준오", "xiirocks");
		saveUser("j.seo1@xiilab.com", "서준오2", "xiirocks1");

		// when
		User findUser = userRepository.findByEmail("j.seo@xiilab.com")
			.orElseThrow(() -> new IllegalStateException("회원이 존재하지 않습니다."));

		// then
		Assertions.assertThat(findUser.getName()).isEqualTo("서준오");
		Assertions.assertThat(passwordEncoder.matches("xiirocks", findUser.getPassword())).isTrue();
	}

	// Boolean existsByEmail(String userEmail);
	@Test
	@DisplayName("이메일 중복 검사")
	void existsByEmail() {
		saveUser("j.seo@xiilab.com", "서준오", "xiirocks");

		Assertions.assertThat(userRepository.existsByEmail("j.seo@xiilab.com")).isTrue();
	}

	private void saveUser(String email, String name, String password) {
		User saveUser = User.builder()
			.email(email)
			.name(name)
			.password(passwordEncoder.encode(password))
			.authProvider(AuthProvider.basic)
			.build();
		userRepository.save(saveUser);
		System.out.println();
		System.out.println();
	}

	// 이메일로 존재여부 검사
	//    Boolean existsByEmail(String userEmail);

}
