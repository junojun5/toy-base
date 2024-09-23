package com.xiilab.data_learnway.data.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xiilab.data_learnway.data.domain.Data;

public interface DataRepository extends JpaRepository<Data, Long> {
}
