package com.xiilab.data_learnway.auth.application.protocols.oauth2.authorization;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.xiilab.data_learnway.global.utils.CookieUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 사용자가 Autorization Code를 발급 받기 위해 전송했던 요청 정보를 세션이 아닌 쿠키에 저장하도록 하는 클래스
@Component
public class HttpCookieOAuth2AuthorizationRequestRepository
	implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
	public static final String REDIRECT_URI_COOKIE_NAME = "OAUTH2_CLIENT_REDIRECT_URI_COOKIE";
	private static final String AUTHORIZATION_REQUEST_COOKIE_NAME = "OAUTH2_GOOGLE_AUTH_REQUEST_COOKIE";
	private static final Integer MAX_AGE = 180;
	private static final String COOKIE_PATH = "/data_learnway";

	// 쿠키로 생성된 Authorization Request를 가져와 역직렬화 후 반환
	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		return CookieUtils.getCookie(request, AUTHORIZATION_REQUEST_COOKIE_NAME)
			.map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
			.orElse(null);
	}

	/**
	 * Authorization Request를 base64로 직렬화하여 쿠키에 저장
	 * Authorization Request Ex) https://authorization-server.com/auth?response_type=code
	 * &client_id=29352735982374239857
	 * &redirect_uri=https://example-app.com/callback
	 * &scope=create+delete
	 * &state=xcoivjuywkdkhvusuye3kch
	 * */
	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
		HttpServletResponse response) {
		// authorizationRequest 없을 떄, 쿠키 있으면 삭제
		if (authorizationRequest == null) {
			CookieUtils.deleteCookie(request, response, AUTHORIZATION_REQUEST_COOKIE_NAME, COOKIE_PATH);
			CookieUtils.deleteCookie(request, response, REDIRECT_URI_COOKIE_NAME, COOKIE_PATH);
		}

		// authorizationRequest를 base64로 직렬화한 값을 쿠키로 생성
		// 직렬화 하는 이유? 바인딩 된 객체를 그대로 저장할 수 없기 때문에, 직렬화 후 쿠키에 저장
		CookieUtils.addCookie(response, AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest),
			COOKIE_PATH, MAX_AGE);

		/**
		 * 사용자를 redirect시킬 URI를 쿠키로 저장
		 * OAuth2의 redirect_uri과는 다름
		 * OAuth2의 redirect_uri은 발급받은 Authorization Code를 통해 access 토큰을 발급받기 위한 redirect uri 경로임
		 * 해당 redirect_uri는 모든 인증 과정이 성공적으로 마무리되면, 사용자를 특정 페이지로 이동시켜주기 위한 uri임
		 * */
		String redirectUriAfterLogin = request.getParameter("redirect_uri");
		if (StringUtils.hasText(redirectUriAfterLogin)) {
			CookieUtils.addCookie(response, REDIRECT_URI_COOKIE_NAME, redirectUriAfterLogin, COOKIE_PATH, MAX_AGE);
		}
	}

	// 저장된 쿠키 삭제
	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
		HttpServletResponse response) {
		return this.loadAuthorizationRequest(request);
	}

	public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.deleteCookie(request, response, AUTHORIZATION_REQUEST_COOKIE_NAME, COOKIE_PATH);
		CookieUtils.deleteCookie(request, response, REDIRECT_URI_COOKIE_NAME, COOKIE_PATH);
	}
}
