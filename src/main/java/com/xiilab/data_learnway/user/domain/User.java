package com.xiilab.data_learnway.user.domain;

import com.xiilab.data_learnway.auth.application.protocols.oauth2.userinfo.OAuth2UserInfo;
import com.xiilab.data_learnway.auth.domain.enums.AuthProvider;
import com.xiilab.data_learnway.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "TB_USER")
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Long id;

	@NotBlank
	@Email
	@Size(max = 100)
	@Column(name = "EMAIL", nullable = false)
	private String email;

	@NotBlank
	@Size(max = 50)
	@Column(name = "NAME", nullable = false)
	private String name;

	// @NotBlank
	@Size(max = 120)
	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "REFRESH_TOKEN")
	@Size(max = 255)
	private String refreshToken;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "AUTH_PROVIDER", nullable = false)
	private AuthProvider authProvider;

	@Column(name = "PROVIDER_ID")
	private String providerId;

	@Builder
	public User(Long id, String email, String name, String password, String refreshToken, AuthProvider authProvider,
		String providerId) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
		this.refreshToken = refreshToken;
		this.authProvider = authProvider;
		this.providerId = providerId;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void updateOAuth2UserInfo(OAuth2UserInfo oAuth2UserInfo) {
		this.name = oAuth2UserInfo.getName();
	}
}
