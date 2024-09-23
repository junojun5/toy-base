package com.xiilab.data_learnway.data.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiilab.data_learnway.data.application.DataService;
import com.xiilab.data_learnway.data.application.dto.request.InsertDataRequest;
import com.xiilab.data_learnway.global.common.ResponseSingleResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/data")
public class DataController {
	private final DataService dataService;

	@PostMapping
	public ResponseSingleResult<Object> insertDataByBasic(InsertDataRequest insertDataRequest) {
		dataService.insertDataByBasic(insertDataRequest);
		ResponseSingleResult<Object> result = new ResponseSingleResult<>();
		result.setResultCode(HttpStatus.OK.value());

		return result;
	}

	@RequestMapping("/tus/**")
	public ResponseSingleResult<Object> insertDataByTus(HttpServletRequest request,
		HttpServletResponse response) {
		dataService.insertDataByTus(request, response);

		ResponseSingleResult<Object> result = new ResponseSingleResult<>();
		result.setResultCode(HttpStatus.OK.value());

		return result;
	}

	@GetMapping("/google/download")
	public ResponseSingleResult<Object> insertDataByGoogleDrive(String accessToken, String gDriveFileIds) {
		dataService.insertDataByGoogleDrive(gDriveFileIds, accessToken);

		ResponseSingleResult<Object> result = new ResponseSingleResult<>();
		result.setResultCode(HttpStatus.OK.value());

		return result;
	}
}
