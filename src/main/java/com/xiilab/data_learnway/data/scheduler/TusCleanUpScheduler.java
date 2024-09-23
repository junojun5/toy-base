package com.xiilab.data_learnway.data.scheduler;

import java.io.IOException;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class TusCleanUpScheduler {
	private final TusFileUploadService tusFileUploadService;

	// 하루 단위로 tus 프로토콜에서 생성한 메타데이터 임시 파일 삭제
	@Scheduled(fixedRate = 60 * 1000 * 1440)
	public void cleanup() throws IOException {
		tusFileUploadService.cleanup();
	}
}
