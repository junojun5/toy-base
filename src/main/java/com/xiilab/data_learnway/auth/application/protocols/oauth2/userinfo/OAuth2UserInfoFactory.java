package com.xiilab.data_learnway.auth.application.protocols.oauth2.userinfo;

import java.util.Map;

import com.xiilab.data_learnway.auth.application.protocols.oauth2.authentication.exception.OAuth2AuthenticationProcessingException;

public class OAuth2UserInfoFactory {
	public static OAuth2UserInfo getOAuth2UserInfo(String providerName, Map<String, Object> attributes) {
		if ("google".equals(providerName)) {
			return new GoogleOAuth2UserInfo(attributes);
		} else {
			throw new OAuth2AuthenticationProcessingException("지원하지 않는 로그인입니다.");
		}
	}
}
