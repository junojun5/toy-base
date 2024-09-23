package com.xiilab.data_learnway.auth.application.protocols.jwt.handler;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiilab.data_learnway.global.config.CommonMessageUtils;
import com.xiilab.data_learnway.global.exception.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 인증되지 않은 사용자가 보안 HTTP 리소스를 요청할 때마다 트리거되고 발생
 * */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthEntryPointHandler implements AuthenticationEntryPoint {
	private final CommonMessageUtils commonMessageUtils;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException)
		throws IOException {

		// Enumeration<String> headers = request.getHeaderNames();
		// Collections.list(headers).stream().forEach(name -> {
		// 	Enumeration<String> values = request.getHeaders(name);
		// 	Collections.list(values).stream().forEach(value -> System.out.println(name + "=" + value));
		// });

		log.warn("Unauthorized error: {}", authException.getMessage());

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		ErrorResponse errorResponse = ErrorResponse.builder()
			.resultCode(HttpServletResponse.SC_UNAUTHORIZED)
			//                .resultMsg("사용자 인증에 실패하였습니다.")
			.resultMsg(commonMessageUtils.getMessage("jwt.fail"))
			.build();

		//        final Map<String, Object> body = new HashMap<>();
		//        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
		//        body.put("error", "Unauthorized");
		//        body.put("message", authException.getMessage());
		//        body.put("path", request.getServletPath());

		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), errorResponse);
	}

}
