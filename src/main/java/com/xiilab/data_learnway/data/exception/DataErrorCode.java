package com.xiilab.data_learnway.data.exception;

import org.springframework.http.HttpStatus;

import com.xiilab.data_learnway.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DataErrorCode implements ErrorCode {
	UPLOAD_FAILED_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "업로드에 실패하였습니다."),
	GDRIVE_NOT_FOUND_CREDENTIALS(HttpStatus.INTERNAL_SERVER_ERROR.value(),
		"GCP 사용자 인증에 실패했습니다.\n애플리케이션이 배포된 서버의 환경변수를 확인해주세요."),
	GDRIVE_INFO_LOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(),
		"구글 드라이브에 저장된 정보를 불러오는데 실패했습니다.\n구글 드라이브 재연결 후 다시 시도해주세요."),
	GDRIVE_FILE_TYPE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(),
		"구글 드라이브에 저장된 파일형식이 손상되어 등록할 수 없습니다.\n구글 드라이브에 파일 재업로드 후 다시 시도해주세요."),
	GDRIVE_FILE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(),
		"파일을 저장하는 중에 오류가 발생했습니다."),
	TUS_FILE_NAME_ERROR_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "저장하려는 원본 파일이 손상되어 업로드에 실패했습니다."),
	TUS_UPLOAD_FAILED_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "저장하려는 원본 파일이 손상되어 업로드에 실패했습니다.");

	private final int code;
	private final String message;

}
