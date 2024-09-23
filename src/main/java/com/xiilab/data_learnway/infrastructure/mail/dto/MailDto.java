package com.xiilab.data_learnway.infrastructure.mail.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// @AllArgsConstructor
public class MailDto<T> {
	private String to;
	private String subject;
	private T model;
}
