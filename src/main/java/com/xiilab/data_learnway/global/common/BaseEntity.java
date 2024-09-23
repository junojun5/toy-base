package com.xiilab.data_learnway.global.common;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
	@CreatedBy
	@Column(name = "FIRST_INS_ACCOUNT")
	private Long createdId;

	@LastModifiedBy
	@Column(name = "LAST_UPD_ACCOUNT")
	private Long updatedId;

	@CreatedDate
	@Column(name = "FIRST_INS_DTM", updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "LAST_UPD_DTM")
	private LocalDateTime updatedAt;

	@Setter
	@Column(name = "USE_STATUS", nullable = false)
	private String useStatus = "U";

	// @PrePersist
	// public void onPrePersist() {
	// 	String customLocalDateTimeFormat = LocalDateTime.now()
	// 		.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	// 	LocalDateTime createdAt = LocalDateTime.parse(customLocalDateTimeFormat,
	// 		DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	// 	this.createdAt = createdAt;
	// 	this.updatedAt = createdAt;
	// }
	//
	// @PreUpdate
	// public void onPreUpdate() {
	// 	String customLocalDateTimeFormat = LocalDateTime.now()
	// 		.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	// 	LocalDateTime updatedAt = LocalDateTime.parse(customLocalDateTimeFormat,
	// 		DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	// 	this.updatedAt = updatedAt;
	// }
}
