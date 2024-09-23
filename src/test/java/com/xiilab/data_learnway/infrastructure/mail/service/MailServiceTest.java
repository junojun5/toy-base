package com.xiilab.data_learnway.infrastructure.mail.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@SpringBootTest
public class MailServiceTest {
	@Autowired
	MailSender mailSender;

	@Test
	@DisplayName("메일 전송 확인을 위한 테스트 코드")
	void sendMail() {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setSubject("STMP서버 메일 전송 확인을 위한 메일입니다.");
		simpleMailMessage.setText("테스트 메일입니다.");
		simpleMailMessage.setFrom("test@test.com");
		simpleMailMessage.setTo("juno8469@naver.com");
		Assertions.assertThatCode(() -> mailSender.send(simpleMailMessage))
			.doesNotThrowAnyException();
	}
}
