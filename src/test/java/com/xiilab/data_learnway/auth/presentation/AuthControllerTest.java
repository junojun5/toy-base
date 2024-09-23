package com.xiilab.data_learnway.auth.presentation;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.xiilab.data_learnway.auth.application.AuthService;
import com.xiilab.data_learnway.auth.application.dto.request.LoginRequest;
import com.xiilab.data_learnway.auth.application.dto.request.SignupRequest;
import com.xiilab.data_learnway.auth.application.protocols.jwt.utils.JwtUtils;
import com.xiilab.data_learnway.user.domain.repository.UserRepository;
import com.xiilab.data_learnway.common.annotation.ControllerTest;
import com.xiilab.data_learnway.global.context.WithUser;

import jakarta.servlet.http.Cookie;

class AuthControllerTest extends ControllerTest {

	@Autowired
	MockMvc mockMvc;

	@SpyBean
	AuthService authService;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	UserRepository userRepository;

	Cookie getCookie() {
		return new Cookie("data_learnway", jwtUtils.generateAccessTokenFromUserId(0L));
	}

	@AfterEach
	void cleanUp() {
		userRepository.deleteAll();
	}

	@Test
	@WithUser
	void login() throws Exception {
		LoginRequest loginRequest = new LoginRequest("test@test.com", "test");

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/login")
				.cookie(getCookie())
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(loginRequest)))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcResultHandlers.print())
			.andDo(MockMvcRestDocumentationWrapper.document("사용자 로그인", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()), resource(ResourceSnippetParameters.builder()
					.tag("AuthController")
					.description("사용자 로그인")
					.requestSchema(Schema.schema("로그인 Request 객체"))
					.responseSchema(Schema.schema("로그인 Response 객체"))
					.requestFields(fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
						fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"))
					.responseFields(fieldWithPath("resultData.id").type(JsonFieldType.NUMBER).description("아이디"),
						fieldWithPath("resultData.userName").type(JsonFieldType.STRING).description("사용자이름"),
						fieldWithPath("resultData.userEmail").type(JsonFieldType.STRING).description("이메일"),
						fieldWithPath("resultCode").type(JsonFieldType.NUMBER).description("응답코드"))
					.build())));
	}

	@Test
	void signup() throws Exception {
		SignupRequest signupRequest = new SignupRequest("test@test.com", "test", "test", "test");

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/signup")
				.cookie(getCookie())
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(signupRequest)))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcResultHandlers.print())
			.andDo(MockMvcRestDocumentationWrapper.document("사용자 회원가입", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()), resource(ResourceSnippetParameters.builder()
					.tag("AuthController")
					.description("사용자 회원가입")
					.requestSchema(Schema.schema("회원가입 Request 객체"))
					.responseSchema(Schema.schema("회원가입 Response 객체"))
					.requestFields(fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
						fieldWithPath("userName").type(JsonFieldType.STRING).description("사용자명"),
						fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
						fieldWithPath("passwordConfirm").type(JsonFieldType.STRING).description("비밀번호 확인"))
					.responseFields(fieldWithPath("resultCode").type(JsonFieldType.NUMBER).description("응답 코드"),
						fieldWithPath("resultData").type(JsonFieldType.NULL).description("응답 데이터"))
					.build())));
	}

	@Test
	@WithUser
	void logout() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/auth/logout").cookie(getCookie()).contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcResultHandlers.print());
	}
}
