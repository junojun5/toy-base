package com.xiilab.data_learnway.auth.application.protocols.oauth2.service;

import java.util.Optional;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.xiilab.data_learnway.auth.application.protocols.oauth2.authentication.exception.OAuth2AuthenticationProcessingException;
import com.xiilab.data_learnway.auth.application.protocols.oauth2.userinfo.OAuth2UserInfo;
import com.xiilab.data_learnway.auth.application.protocols.oauth2.userinfo.OAuth2UserInfoFactory;
import com.xiilab.data_learnway.auth.application.protocols.user.UserDetailsImpl;
import com.xiilab.data_learnway.auth.domain.enums.AuthProvider;
import com.xiilab.data_learnway.user.domain.User;
import com.xiilab.data_learnway.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Provider 로부터 획득한 유저 정보를 다룰 Class
 * */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
		// request에 저장된 유저정보를 "OAuth2User" 객체로 변환
		OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
		try {
			return processOAuth2User(oAuth2UserRequest, oAuth2User);
		} catch (AuthenticationException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
		}
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
		String providerName = oAuth2UserRequest.getClientRegistration().getRegistrationId();

		// provider(Ex) google, naver...)별로 다른 타입 return
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerName,
			oAuth2User.getAttributes());
		// oAuth2User.getAttributes()에 email 없으면 throw
		if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
			throw new OAuth2AuthenticationProcessingException("인증 서버 응답에 email 주소가 존재하지 않습니다.");
		}

		// DB에 저장된 사용자 인증 공급자와 request로 받은 인증 공급자 다르면 throw
		Optional<User> findUser = userRepository.findByEmail(oAuth2UserInfo.getEmail());
		User user = null;
		if (findUser.isPresent()) {
			user = findUser.get();
			if (!user.getAuthProvider().equals(AuthProvider.valueOf(providerName))) {
				throw new OAuth2AuthenticationProcessingException(
					"providerName" + "로그인으로 가입된 회원이 아닙니다.\n다른 방식으로 로그인 해주세요.");
			}

			// 회원정보 DB에 있으면 리소스 서버에 저장된 최신정보로 update
			user = updateUser(user, oAuth2UserInfo);
		} else {
			// 최초 로그인 사용자 회원가입
			// 회원정보 DB에 없으면 리소스 서버에 저장된 정보로 insert
			user = insertUser(providerName, oAuth2UserInfo);
		}

		return UserDetailsImpl.build(user, oAuth2User.getAttributes());
	}

	private User insertUser(String providerName, OAuth2UserInfo oAuth2UserInfo) {
		User insertUser = User.builder()
			.email(oAuth2UserInfo.getEmail())
			.name(oAuth2UserInfo.getName())
			.authProvider(AuthProvider.valueOf(providerName))
			.providerId(oAuth2UserInfo.getId())
			.build();
		return userRepository.save(insertUser);
	}

	private User updateUser(User updateUser, OAuth2UserInfo oAuth2UserInfo) {
		updateUser.updateOAuth2UserInfo(oAuth2UserInfo);
		return userRepository.save(updateUser);
	}
}
