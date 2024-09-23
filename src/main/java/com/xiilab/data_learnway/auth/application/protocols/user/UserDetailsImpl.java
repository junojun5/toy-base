package com.xiilab.data_learnway.auth.application.protocols.user;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiilab.data_learnway.user.domain.User;

public class UserDetailsImpl implements OAuth2User, UserDetails {
	private Long id;
	private String email;
	private String name;
	@JsonIgnore
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	private Map<String, Object> attributes;

	public UserDetailsImpl(Long id, String email, String name, String password,
		Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
		this.authorities = authorities;
	}

	public static UserDetailsImpl build(User user) {
		// 권한처리
		//        List<GrantedAuthority> authorities = user.getRoles().stream()
		//                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
		//                .collect(Collectors.toList());

		return new UserDetailsImpl(
			user.getId(),
			user.getEmail(),
			user.getName(),
			user.getPassword(),
			null);
	}

	public static UserDetailsImpl build(User user, Map<String, Object> attributes) {
		UserDetailsImpl userDetails = UserDetailsImpl.build(user);
		userDetails.setAttributes(attributes);
		return userDetails;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	// 사용자에게 부여된 권한을 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	// 인증하는데 사용되는 암호 반환
	@Override
	public String getPassword() {
		return password;
	}

	// 인증하는데 사용되는 사용자 이름을 반환
	@Override
	public String getUsername() {
		return String.valueOf(id);
	}

	/**
	 * 사용자 계정만료여부 반환
	 * true: 만료 X
	 * false: 만료 O
	 * */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 사용자 계정잠금여부 반환
	 * true: 잠금 X
	 * false: 잠금 O
	 * */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 사용자 자격증명만료 여부 반환
	 * true: 패스워드 만료 X
	 * false: 패스워드 만료 O
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 사용자 사용가능여부 반환
	 * true: 사용가능 O
	 * false: 사용가능 X
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
}
