package com.xiilab.data_learnway.common;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.xiilab.data_learnway.auth.application.protocols.user.UserDetailsImpl;
import com.xiilab.data_learnway.global.context.WithUser;
import com.xiilab.data_learnway.user.domain.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@SpringBootTest
public class JwtUtilsTest {
	@Value("${spring.security.jwt.token.secret-key}")
	private String jwtSecretKey;

	@Value("${spring.security.jwt.token.access.expire-length}")
	private Long jwtExpirationMs;

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Test
	@WithUser
	void generateJwtToken() {
		// User user = User.builder()
		// 	.email("testId@naver.com")
		// 	.name("test")
		// 	.password(passwordEncoder.encode("pass"))
		// 	.authProvider(AuthProvider.valueOf("basic"))
		// 	.build();
		// userRepository.save(user);
		//
		// Authentication authentication = authenticationManager.authenticate(
		// 	new UsernamePasswordAuthenticationToken("testId@naver.com", "pass"));

		Authentication authentication = SecurityContextHolder
			.getContext()
			.getAuthentication();
		UserDetailsImpl userPrincipal = (UserDetailsImpl)authentication.getPrincipal();

		String jwt = Jwts.builder()
			.setSubject((userPrincipal.getUsername()))
			.setIssuedAt(new Date())
			.setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
			.signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey)),
				SignatureAlgorithm.HS256)
			.compact();

		System.out.println(jwt);
	}
}
