package com.xiilab.data_learnway.infrastructure.mail.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.xiilab.data_learnway.infrastructure.mail.dto.MailDto;
import com.xiilab.data_learnway.infrastructure.mail.event.SignupSuccessEvent;
import com.xiilab.data_learnway.infrastructure.mail.service.MailService;
import com.xiilab.data_learnway.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailEventListener {
	private final MailService mailService;

	//@EventListener
	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleSignupSuccessSending(SignupSuccessEvent signupSuccessEvent) {
		MailDto<User> mailDto = new MailDto<>();
		mailDto.setTo(signupSuccessEvent.getUser().getEmail());
		mailDto.setSubject("[Data Learnway] " + signupSuccessEvent.getUser().getName() + "님의 회원가입을 축하드립니다.");
		mailDto.setModel(signupSuccessEvent.getUser());

		try {
			mailService.sendMailInHtml("email/signupSuccess", mailDto);
		} catch (Exception e) {
			log.error("User registration successful Email sending failed!\n{0}", e);
		}
	}
}
