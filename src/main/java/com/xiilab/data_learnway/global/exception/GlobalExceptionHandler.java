package com.xiilab.data_learnway.global.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.xiilab.data_learnway.global.config.CommonMessageUtils;
import com.xiilab.data_learnway.global.exception.ErrorResponse.ValidationError;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private final CommonMessageUtils commonMessageUtils;

	@ExceptionHandler(RestApiException.class)
	public ResponseEntity<Object> handleCustomException(RestApiException e) {
		ErrorCode errorCode = e.getErrorCode();
		return handleExceptionInternal(errorCode);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
		log.warn("handleIllegalArgument", e);
		ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
		return handleExceptionInternal(errorCode, e.getMessage());
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> handleAllException(Exception ex) {
		log.warn("handleAllException", ex);
		log.warn(String.valueOf(ex.getStackTrace()));
		ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
		return handleExceptionInternal(errorCode);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleControllerArgumentNotValid(ConstraintViolationException ex) {
		log.warn("handleControllerArgumentNotValid", ex);
		ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
		return handleExceptionInternal(ex, errorCode);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
		WebRequest request) {
		log.warn("handleIllegalArgument", ex);
		ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
		return handleExceptionInternal(ex, errorCode);
	}

	private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getCode())
			.body(makeErrorResponse(errorCode));
	}

	private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
		return ResponseEntity.status(errorCode.getCode())
			.body(makeErrorResponse(errorCode, message));
	}

	private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getCode())
			.body(makeErrorResponse(e, errorCode));
	}

	private ResponseEntity<Object> handleExceptionInternal(ConstraintViolationException ex,
		ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getCode())
			.body(makeErrorResponse(ex, errorCode));
	}

	private ErrorResponse makeErrorResponse(final ErrorCode errorCode) {
		return ErrorResponse.builder()
			.resultCode(errorCode.getCode())
			//            .resultMsg(errorCode.getMessage())
			.resultMsg(commonMessageUtils.getMessage(errorCode.getMessage()))
			.build();
	}

	private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
		return ErrorResponse.builder()
			.resultCode(errorCode.getCode())
			//            .resultMsg(message)
			.resultMsg(commonMessageUtils.getMessage(message))
			.build();
	}

	private ErrorResponse makeErrorResponse(BindException e, ErrorCode errorCode) {
		List<ValidationError> errorList = e.getBindingResult().getFieldErrors()
			.stream()
			.map(ValidationError::of)
			.collect(Collectors.toList());
		return ErrorResponse.builder()
			.resultCode(errorCode.getCode())
			//            .resultMsg(errorCode.getMessage())
			.resultMsg(commonMessageUtils.getMessage(errorCode.getMessage()))
			.errors(errorList)
			.build();
	}

	private ErrorResponse makeErrorResponse(ConstraintViolationException ex, ErrorCode errorCode) {
		List<ValidationError> errorList = new ArrayList<>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			String field = violation.getPropertyPath().toString().split("\\.")[1];
			String message = violation.getMessage();
			errorList.add(ValidationError.of(field, message));
		}
		return ErrorResponse.builder()
			.resultCode(errorCode.getCode())
			//            .resultMsg(errorCode.getMessage())
			.resultMsg(commonMessageUtils.getMessage(errorCode.getMessage()))
			.errors(errorList)
			.build();
	}
}
