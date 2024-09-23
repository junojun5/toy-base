package com.xiilab.data_learnway.data.domain;

import com.xiilab.data_learnway.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_DATASET")
@Getter
public class Dataset extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DATASET_ID")
	private Long id;
	@Column(name = "DATASET_NAME")
	private String name;
	@Column(name = "DATASET_DESC")
	private String description;

	@Builder
	public Dataset(Long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
}
