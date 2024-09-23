package com.xiilab.data_learnway.data.application.dto.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InsertDataRequest {
	@NotEmpty(message = "업로드할 파일이 존재하지 않습니다.")
	private MultipartFile[] files;
}
