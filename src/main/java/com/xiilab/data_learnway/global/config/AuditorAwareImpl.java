package com.xiilab.data_learnway.global.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.xiilab.data_learnway.auth.application.protocols.user.UserDetailsImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<Long> {

	@Override
	public Optional<Long> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder
			.getContext()
			.getAuthentication();

		if (authentication == null || !authentication.isAuthenticated() ||
			authentication.getPrincipal().equals("anonymousUser")) {
			return Optional.empty();
		}

		UserDetailsImpl user = (UserDetailsImpl)authentication.getPrincipal();
		return Optional.ofNullable(user.getId());
	}
}
