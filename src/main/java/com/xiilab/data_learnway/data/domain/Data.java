package com.xiilab.data_learnway.data.domain;

import com.xiilab.data_learnway.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_DATA")
@Getter
public class Data extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DATA_ID")
	private Long id;
	// @Column(name = "DATASET_ID")
	// private Long datasetId;
	@Column(name = "SAVE_PATH")
	private String savePath;
	@Column(name = "SAVE_FILENAME")
	private String saveFileName;
	@Column(name = "ORIGIN_FILENAME")
	private String originFileName;
	@Column(name = "DATA_SIZE")
	private Long dataSize;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DATASET_ID")
	private Dataset dataset;

	@Builder
	public Data(Long id, String savePath, String saveFileName, String originFileName, Long dataSize, Dataset dataset) {
		this.id = id;
		this.savePath = savePath;
		this.saveFileName = saveFileName;
		this.originFileName = originFileName;
		this.dataSize = dataSize;
		this.dataset = dataset;
	}

	public void addDataset(Dataset dataset) {
		this.dataset = dataset;
	}
}
