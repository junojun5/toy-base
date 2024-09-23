package com.xiilab.data_learnway.infrastructure.mail.event;

import com.xiilab.data_learnway.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupSuccessEvent {
	private User user;
}
