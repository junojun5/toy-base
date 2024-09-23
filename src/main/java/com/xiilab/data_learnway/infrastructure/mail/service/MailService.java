package com.xiilab.data_learnway.infrastructure.mail.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.xiilab.data_learnway.infrastructure.mail.dto.MailDto;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
	@Value("${spring.mail.username}")
	private String FROM_ADDRESS;
	private final JavaMailSender javaMailSender;
	private final SpringTemplateEngine templateEngine;

	public void sendMailInHtml(String htmlLocation, MailDto mailDto) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
		mimeMessageHelper.setFrom(FROM_ADDRESS); // 메일 발신자
		mimeMessageHelper.setTo(mailDto.getTo()); // 메일 수신자
		mimeMessageHelper.setSubject(mailDto.getSubject()); // 메일 제목
		mimeMessageHelper.setText(setContext(htmlLocation, mailDto.getModel()), true); // 메일 본문 내용, HTML 여부
		javaMailSender.send(mimeMessage);
		log.info("Email sent successfully!");
	}

	// thymeleaf를 통한 html 적용
	public String setContext(String htmlLocation, Object model) {
		Context context = new Context();
		context.setVariable("model", model);
		return templateEngine.process(htmlLocation, context);
	}
}
