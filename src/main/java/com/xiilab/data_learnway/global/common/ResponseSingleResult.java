package com.xiilab.data_learnway.global.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSingleResult<T> {
	private T resultData;
	private int resultCode;
}
