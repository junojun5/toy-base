package com.xiilab.data_learnway.test.presentation;

import static com.xiilab.data_learnway.test.domain.dto.TestDto.*;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiilab.data_learnway.global.common.ResponseSingleResult;
import com.xiilab.data_learnway.global.exception.RestApiException;
import com.xiilab.data_learnway.test.application.TestService;
import com.xiilab.data_learnway.test.domain.dto.TestSearchCondition;
import com.xiilab.data_learnway.test.exception.TestErrorCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Tag(name = "TestController", description = "테스트 컨트롤러")
@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {

	private final TestService testService;

	@GetMapping("/test")
	public String test() {
		throw new RestApiException(TestErrorCode.TEST_ERROR);
	}

	@GetMapping("/test1")
	public String test1() {
		throw new IllegalArgumentException("일리걸아규먼트익셉션");
	}

	@PostMapping("/test2")
	public RequestTestDto test2(@Valid @RequestBody RequestTestDto requestTestDto) {
		return requestTestDto;
	}

	@GetMapping("/test3/{testId}/{age}")
	public void test3(@PathVariable @Min(1) Long testId, @PathVariable @Min(1) Long age) {
	}

	@GetMapping("/test4")
	public String test4() {
		throw new NullPointerException();
	}

	@Operation(operationId = "insertTest", summary = "테스트 등록", description = "테스트를 등록한다.", tags = {"TestController"})
	@PostMapping("/tests")
	public ResponseSingleResult<Long> insertTest(@Valid @RequestBody RequestInsertTest request,
		HttpServletRequest httpServletRequest) {

		Long saveId = testService.insertTest(request);
		ResponseSingleResult<Long> response = new ResponseSingleResult<>();
		response.setResultData(saveId);
		response.setResultCode(HttpStatus.OK.value());
		//Enumeration<String> attributeNames = session.getAttributeNames();
		return response;
	}

	@GetMapping("/tests/{testId}")
	public ResponseSingleResult<ResponseTestWithTeam> getTestById(@PathVariable Long testId) {
		ResponseTestWithTeam testById = testService.getTestById(testId);

		ResponseSingleResult<ResponseTestWithTeam> response = new ResponseSingleResult<>();
		response.setResultCode(HttpStatus.OK.value());
		response.setResultData(testById);
		return response;
	}

	@GetMapping("/tests")
	public ResponseSingleResult<ResponsePagingTestWithTeam> findAll(@Valid final Pageable pageable,
		TestSearchCondition condition, HttpServletRequest request) {

		ResponsePagingTestWithTeam tests = testService.getTestList(pageable, condition);

		ResponseSingleResult<ResponsePagingTestWithTeam> response = new ResponseSingleResult<>();
		response.setResultCode(HttpStatus.OK.value());
		response.setResultData(tests);
		return response;
	}

	@PutMapping("/tests/{testId}")
	public ResponseSingleResult<Long> updateTest(@Valid @RequestBody RequestUpdateTest request,
		@PathVariable Long testId) {
		Long updateId = testService.updateTest(request, testId);
		ResponseSingleResult<Long> response = new ResponseSingleResult<>();
		response.setResultCode(HttpStatus.OK.value());
		response.setResultData(updateId);
		return response;
	}

	//    @DeleteMapping("/tests/{testId}")
	//    public ResponseSingleResult<Long> deleteTest(@PathVariable Long testId){
	//        Long deleteId = testService.deleteTest(testId);
	//        ResponseSingleResult<Long> response = new ResponseSingleResult<>();
	//        response.setResultCode(HttpStatus.OK.value());
	//        response.setResultData(deleteId);
	//        return response;
	//    }
	@DeleteMapping("/tests/{testId}")
	public ResponseSingleResult<Long> deleteTest2(@PathVariable Long testId) {
		Long deleteId = testService.deleteTest2(testId);
		ResponseSingleResult<Long> response = new ResponseSingleResult<>();
		response.setResultCode(HttpStatus.OK.value());
		response.setResultData(deleteId);
		return response;
	}

}
