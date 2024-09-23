package com.xiilab.data_learnway.test.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.xiilab.data_learnway.test.domain.Test;
import com.xiilab.data_learnway.test.domain.dto.TestSearchCondition;

public interface TestRepositoryCustom {
	Page<Test> getTests(PageRequest pageRequest, TestSearchCondition condition);
}
