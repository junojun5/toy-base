package com.xiilab.data_learnway.auth.application.protocols.config;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.xiilab.data_learnway.auth.application.protocols.jwt.filter.AuthTokenFilter;
import com.xiilab.data_learnway.auth.application.protocols.jwt.handler.AuthEntryPointHandler;
import com.xiilab.data_learnway.auth.application.protocols.jwt.service.UserDetailsServiceImpl;
import com.xiilab.data_learnway.auth.application.protocols.oauth2.authentication.handler.OAuth2AuthenticationFailureHandler;
import com.xiilab.data_learnway.auth.application.protocols.oauth2.authentication.handler.OAuth2AuthenticationSuccessHandler;
import com.xiilab.data_learnway.auth.application.protocols.oauth2.authorization.HttpCookieOAuth2AuthorizationRequestRepository;
import com.xiilab.data_learnway.auth.application.protocols.oauth2.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	// DB에서 찾은 사용자 정보를 처리하는 UserDetailService의 구현체
	private final UserDetailsServiceImpl userDetailsService;
	// 인증, 인가 예외를 처리하기 위한 사용자 정의 클래스
	private final AuthEntryPointHandler unauthorizedHandler;
	// 요청 로그인 정보를 가로채 UsernamePasswordAuthenticationToken을 생성
	private final AuthTokenFilter authTokenFilter;
	// OAuth2 인증을 수행할 커스텀 서비스
	private final CustomOAuth2UserService customOAuth2UserService;
	// OAuth2 인증 성공시, 수행하는 Handler
	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
	// OAuth2 인증 실패시, 수행하는 Handler
	private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
	private final ClientRegistrationRepository clientRegistrationRepository;

	/**
	 * bycrypt 해시 함수를 사용하여 암호화 및 복호화
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 시큐리티 처리과정 3)
	 * 인증을 위해 사용되며, 인증 프로세스에 필요
	 * 구현체인 AuthenticationProvier를 찾아 처리를 위임
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/**
	 * 시큐리티 처리과정 4)
	 * UserDetailsService 및 PasswordEncoder를 사용하여 사용자 아이디와 암호를 인증하는 AuthenticationProvider 구현체
	 **/
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		// 시큐리티 처리과정 5)
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	/**
	 * 세션 대신 쿠키에 Autorization Request를 저장하기 위한 클래스
	 * 쿠키에 저장하는 이유? JWT는 세션에 저장할 필요가 없기 때문임
	 * */
	@Bean
	public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
		return new HttpCookieOAuth2AuthorizationRequestRepository();
	}

	/**
	 * HTTP 요청에 적용되는 보안 필터를 구성
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
			// exceptionHandling(): 인증 실패시 예외 처리
			.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth ->
				// requestMatchers([URI]).permitAll(): 입력된 URI 인증없이 접근 허용
				auth.requestMatchers("index.html").permitAll()
					.requestMatchers("/images/**").permitAll()
					.requestMatchers("/css/**").permitAll()
					.requestMatchers("/pages/**").permitAll()
					.requestMatchers("/error").permitAll()
					.requestMatchers("/api/auth/login").permitAll()
					.requestMatchers("/api/auth/signup").permitAll()
					.requestMatchers("/api/lang/**").permitAll()
					.requestMatchers("/api/tus/**").permitAll()
					.requestMatchers("/google/**").permitAll()
					.requestMatchers("/oauth2/authorization/**").permitAll()
					.requestMatchers("/login/oauth2/code/**").permitAll()
					.requestMatchers("/v3/api-docs/**").permitAll()
					.requestMatchers("/swagger-ui/**").permitAll()
					.requestMatchers("/api-doc/**").permitAll()
					// anyRequest().authenticated(): requestMatchers가 아닌 모든 URI는 인증 필요
					.anyRequest()
					.authenticated()
			)
			// 소셜 로그인 설정
			.oauth2Login(oauth2 -> oauth2
				/**
				 * 프론트엔드에서 백엔드로 소셜로그인 요청을 보내는 URI, 이 경로로 접근하면 필터를 통해 자동으로 구글 API 로그인 페이지로 리다이렉트 시킴
				 * "/oauth2/authorization/{provider}"는 기본 URI, 변경을 원하면 값 수정
				 * */
				.authorizationEndpoint(authorization -> authorization
					// 인증 과정에서 Athorization Request를 기본으로 Session에 저장하지만 jwt는 session이 필요없기에, Cookie로 저장하도록 하는 설정
					.authorizationRequestRepository(cookieAuthorizationRequestRepository())
					// Authorization Request에 파라미터를 추가하는 resolver
					.authorizationRequestResolver(this.authorizationRequestResolver(clientRegistrationRepository))
				)
				/**
				 * ID, PWD 입력 후 Authorization Code와 함께 토큰 발급 및 신뢰성 확인을 위해 redirect 할 엔드포인트 URI 지정
				 * OAuth2 클라이언트 생성할 때 입력했던 URI입력 되면 됨
				 * 리다이렉트 URI는 사용자에게 보여줄 페이지로 보내기 위한 것이 아님
				 * "/login/oauth2/code/{provider}"는 시큐리티에서 권장하는 기본 URI Ex) /login/oauth2/code/google
				 * */
				.redirectionEndpoint(
					redirect -> redirect
						.baseUri("/login/oauth2/code/**")
				)
				// Auth Provider 로부터 획득한 유저 정보를 다룰 service class를 지정
				.userInfoEndpoint(userInfo -> userInfo
					.userService(customOAuth2UserService)
				)
				// OAuth2 로그인 성공시 호출할 handler
				.successHandler(oAuth2AuthenticationSuccessHandler)
				// OAuth2 로그인 실패시 호출할 handler
				.failureHandler(oAuth2AuthenticationFailureHandler)
			);

		http.authenticationProvider(authenticationProvider());

		// 시큐리티 처리과정 1, 2)
		// UsernamePasswordAuthenticationFilter 이전에 authenticationJwtTokenFilter()실행
		http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	private OAuth2AuthorizationRequestResolver authorizationRequestResolver(
		ClientRegistrationRepository clientRegistrationRepository) {

		DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver =
			new DefaultOAuth2AuthorizationRequestResolver(
				clientRegistrationRepository, "/oauth2/authorization");
		authorizationRequestResolver.setAuthorizationRequestCustomizer(
			authorizationRequestCustomizer());

		return authorizationRequestResolver;
	}

	private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer() {
		return customizer -> customizer
			// 구글 로그인 클릭할 때마다, 계정 선택할 수 있도록 파라미터 추가
			.additionalParameters(params -> params.put("prompt", "consent"));
	}
}
