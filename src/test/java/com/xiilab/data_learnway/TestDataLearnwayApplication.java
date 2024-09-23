package com.xiilab.data_learnway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestDataLearnwayApplication {

	public static void main(String[] args) {
		SpringApplication.from(DataLearnwayApplication::main).with(TestDataLearnwayApplication.class).run(args);
	}

}
