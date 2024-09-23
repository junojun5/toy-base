package com.xiilab.data_learnway.auth.application.protocols.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiilab.data_learnway.auth.application.protocols.user.UserDetailsImpl;
import com.xiilab.data_learnway.user.domain.User;
import com.xiilab.data_learnway.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userEmail) {
		// DB에서 사용자 정보 조회 후 반환
		User user = userRepository.findByEmail(userEmail)
			.orElseThrow(() -> new UsernameNotFoundException("User Not Found with userEmail: " + userEmail));

		return UserDetailsImpl.build(user);
	}
}
