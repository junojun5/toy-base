package com.xiilab.data_learnway.data.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiilab.data_learnway.common.annotation.RepositoryTest;
import com.xiilab.data_learnway.data.domain.Data;
import com.xiilab.data_learnway.data.domain.Dataset;
import com.xiilab.data_learnway.data.domain.repository.DataRepository;
import com.xiilab.data_learnway.data.domain.repository.DatasetRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

class DatasetRepositoryTest extends RepositoryTest {

	@Autowired
	DatasetRepository datasetRepository;

	@Autowired
	DataRepository dataRepository;

	@PersistenceContext
	EntityManager em;

	@Test
	@DisplayName("Hello")
	void getDataList() {
		Dataset dataset = Dataset.builder()
			.name("Hello")
			.build();
		datasetRepository.save(dataset);

		Data data = Data.builder()
			.savePath("hello world!")
			.build();
		dataRepository.save(data);
		data.addDataset(dataset);
	}

	@Test
	void helloWorld() {
		String saveFullFilePath = "dataset/adsdadas.jpg";
		String saveFileName = saveFullFilePath.substring(saveFullFilePath.lastIndexOf("/") + 1);
		System.out.println(saveFileName);
	}

}
