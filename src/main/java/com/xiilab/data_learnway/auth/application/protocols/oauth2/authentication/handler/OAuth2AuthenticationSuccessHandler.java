package com.xiilab.data_learnway.auth.application.protocols.oauth2.authentication.handler;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.xiilab.data_learnway.auth.application.protocols.jwt.utils.JwtUtils;
import com.xiilab.data_learnway.auth.application.protocols.oauth2.authorization.HttpCookieOAuth2AuthorizationRequestRepository;
import com.xiilab.data_learnway.global.utils.CookieUtils;
import com.xiilab.data_learnway.user.domain.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * OAuth2 인증 성공시 접근하는 핸들러
 * 액세스, 리프레쉬 토큰을 재발급 함.
 * */
@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JwtUtils jwtUtils;
	private final UserRepository userRepository;
	private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();

		// DB 수정자 및 시간 갱신을 위해 SecurityContext에 저장
		generateAuthentication(request, userDetails);
		// 사용자 액세스 및 리프레쉬 토큰 발급
		generateTokenByUserId(response, userDetails);

		// 테스트용 (추후에 삭제 해야됨)
		Optional<String> redirectUri = CookieUtils.getCookie(request,
				HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_COOKIE_NAME)
			.map(Cookie::getValue);
		if (redirectUri.isEmpty()) {
			throw new IllegalStateException("Bye World!");
		}

		// Authorization Request 쿠키 및 redirect_uri 쿠키 삭제
		clearAuthenticationAttributes(request, response);

		String targetUrl = UriComponentsBuilder.fromUriString(redirectUri.get())
			.build().toUriString();
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	private void generateTokenByUserId(HttpServletResponse response, UserDetails userDetails) {
		Long userId = Long.parseLong(userDetails.getUsername());
		// 회원 DB에 사용자 저장되어 있으면, 액세스 토큰 쿠키 발급 및 리프레쉬 토큰 업데이트
		userRepository.findById(userId).ifPresent(user -> {
			jwtUtils.generateJwtCookieByUserId(response, userId);
			user.updateRefreshToken(jwtUtils.generateRefreshTokenFromUserId(userId));
		});
	}

	private void generateAuthentication(HttpServletRequest request, UserDetails userDetails) {
		UsernamePasswordAuthenticationToken auth =
			new UsernamePasswordAuthenticationToken(
				userDetails,
				null,
				userDetails.getAuthorities());

		auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
		super.clearAuthenticationAttributes(request);
		httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
	}

}
