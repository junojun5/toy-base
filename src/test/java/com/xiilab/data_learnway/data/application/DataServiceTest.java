package com.xiilab.data_learnway.data.application;

import static org.mockito.BDDMockito.*;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import com.xiilab.data_learnway.common.annotation.ServiceTest;
import com.xiilab.data_learnway.data.application.dto.request.InsertDataRequest;
import com.xiilab.data_learnway.data.domain.repository.DataRepository;
import com.xiilab.data_learnway.global.utils.CustomFileUtils;

class DataServiceTest extends ServiceTest {
	@Mock
	DataRepository dataRepository;

	@InjectMocks
	DataService dataService;

	@Test
	@DisplayName("InsertData 테스트")
	void insertData() throws IOException {

		mockStatic(CustomFileUtils.class);
		BDDMockito.given(CustomFileUtils.saveMultipartFileToFile(any(), any()))
			.willReturn("/dataset/test.jpg");

		MockMultipartFile[] mockMultipartFiles = new MockMultipartFile[10];
		for (int i = 0; i < 10; i++) {
			MockMultipartFile multipartFile = new MockMultipartFile("file" + i, "test" + i + ".txt",
				"text/plain", "Spring Framework".getBytes());
			mockMultipartFiles[i] = multipartFile;
		}

		InsertDataRequest insertDataRequest = new InsertDataRequest(
			mockMultipartFiles);
		dataService.insertDataByBasic(insertDataRequest);

		verify(dataRepository, times(10)).save(any());
	}
}
