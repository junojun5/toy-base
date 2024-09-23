package com.xiilab.data_learnway.data.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xiilab.data_learnway.data.domain.Dataset;

public interface DatasetRepository extends JpaRepository<Dataset, Long> {
}
