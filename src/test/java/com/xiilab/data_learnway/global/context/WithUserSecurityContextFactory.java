package com.xiilab.data_learnway.global.context;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.xiilab.data_learnway.auth.application.protocols.user.UserDetailsImpl;
import com.xiilab.data_learnway.auth.domain.enums.AuthProvider;
import com.xiilab.data_learnway.user.domain.User;
import com.xiilab.data_learnway.user.domain.repository.UserRepository;

public class WithUserSecurityContextFactory implements WithSecurityContextFactory<WithUser> {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public WithUserSecurityContextFactory(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public SecurityContext createSecurityContext(WithUser withUser) {
		// 사용자 등록
		User user = User.builder()
			.id(0L)
			.email(withUser.email())
			.name(withUser.name())
			.password(passwordEncoder.encode(withUser.password()))
			.authProvider(AuthProvider.basic)
			.build();

		userRepository.save(user);

		UserDetails userDetails = UserDetailsImpl.build(user);
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(
				userDetails,
				null,
				userDetails.getAuthorities());

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);

		return context;
	}
}
