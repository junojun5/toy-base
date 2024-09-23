package com.xiilab.data_learnway.auth.application.protocols.jwt.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.xiilab.data_learnway.auth.application.protocols.jwt.service.UserDetailsServiceImpl;
import com.xiilab.data_learnway.auth.application.protocols.jwt.utils.JwtUtils;
import com.xiilab.data_learnway.user.domain.User;
import com.xiilab.data_learnway.user.domain.repository.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 요청 로그인 정보를 가로채 UsernamePasswordAuthenticationToken을 생성
 * */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

	private final JwtUtils jwtUtils;
	private final UserDetailsServiceImpl userDetailsService;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		try {
			// 쿠키에 저장된 액세스 토큰 값 저장
			String jwt = jwtUtils.getJwtFromCookies(request);

			// 액세스 토큰 검증
			if (StringUtils.hasText(jwt) && jwtUtils.validateJwtToken(jwt)) {
				// 토큰 값 파싱 후 ID 추출
				String userId = jwtUtils.getUserIdFromJwtToken(jwt);
				userRepository.findById(Long.parseLong(userId))
					.ifPresent(findUser -> {
							generateAuthentication(request, findUser);
						}
					);
			}
		} catch (ExpiredJwtException e) {   // 액세스 토큰 만료시 리프레쉬 토큰으로 재발급
			log.error("JWT Access token is expired: {}", e.getMessage());

			try {
				// subject에 저장된 ID 저장
				Long userId = Long.parseLong(e.getClaims().getSubject());
				userRepository.findById(userId)
					.ifPresent(findUser -> {
						String refreshJwt = findUser.getRefreshToken();
						// 리프레쉬 토큰 검증 후 액세스 토큰 재발급
						if (StringUtils.hasText(refreshJwt) && jwtUtils.validateJwtToken(refreshJwt)) {
							generateAuthentication(request, findUser);
							jwtUtils.generateJwtCookieByUserId(response, userId);
						}
					});
			} catch (ExpiredJwtException e2) {
				log.error("JWT Refresh token is expired: {}", e2.getMessage());
			} catch (NoSuchElementException | NumberFormatException e2) {
				handleException(e2);
			} catch (Exception e2) {
				log.error("Unknown error reissuing access token: {}", e2);
			}
		} catch (NoSuchElementException | NumberFormatException e2) {
			handleException(e2);
		} catch (Exception e) {
			log.error("Cannot set user authentication: {}", e);
		}

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String[] excludedEndpoints = new String[] {"api/tus"};
		String path = request.getRequestURI();

		return Arrays.stream(excludedEndpoints).anyMatch(path::contains);
	}

	/**
	 * UsernamePasswordAuthenticationToken 발급 및 SecurityContextHolder에 저장
	 * SecurityContextHolder에 잘못된 인증정보 넣으면 AuthEntryPointJwt에서 exception 처리됨
	 * */
	private void generateAuthentication(HttpServletRequest request, User findUser) {
		// DB에서 조회된 유저정보 SecurityContextHolder에 저장
		UserDetails userDetails = userDetailsService.loadUserByUsername(findUser.getEmail());
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(
				userDetails,
				null,
				userDetails.getAuthorities());

		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private boolean handleException(Exception e) {
		if (e instanceof NumberFormatException) {
			log.error("토큰 subject를 숫자로 변환할 수 없습니다: {}", e.getMessage());
		} else if (e instanceof NoSuchElementException) {
			log.error("토큰 subject로 조회되는 사용자 정보가 존재하지 않습니다: {}", e.getMessage());
		} else {
			return false;
		}

		return true;
	}

}
