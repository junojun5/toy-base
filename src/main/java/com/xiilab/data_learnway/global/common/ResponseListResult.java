package com.xiilab.data_learnway.global.common;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseListResult<T> {
	private List<T> resultData;
	private int resultCode;
}
