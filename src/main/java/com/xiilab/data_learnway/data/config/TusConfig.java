package com.xiilab.data_learnway.data.config;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xiilab.data_learnway.global.enums.DirMidPath;
import com.xiilab.data_learnway.global.utils.CustomFileUtils;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;

@Slf4j
@Configuration
public class TusConfig {
	// 업로드 재개를 위한 메타 데이터를 저장하는 디렉토리 경로
	private final String tusStoragePath =
		CustomFileUtils.getRootFilePath() + File.separator + DirMidPath.TUS_META_MID_PATH.getPath();
	// 업로드 재개 가능한 만료시간
	private final Long tusExpirationPeriod = 60 * 1000 * 1440L;    // 1day

	@Value("${server.servlet.context-path}")
	private String contextPath;

	/**
	 * 스프링 컨테이너에 등록된 빈 제거할 때, 메타 데이터 디렉토리에 저장된 파일 전체 삭제
	 * */
	@PreDestroy
	public void exit() throws IOException {
		log.info("Good Bye Data learnway!");
		// cleanup any expired uploads and stale locks
		tusFileUploadService().cleanup();
	}

	@Bean
	public TusFileUploadService tusFileUploadService() {
		return new TusFileUploadService()
			// 업로드 정보 보관 경로
			.withStoragePath(tusStoragePath)
			// 업로드된 바이트를 다운로드할 수 있는 비공식 확장 프로그램 활성화
			.withDownloadFeature()
			// 만료시간 설정
			.withUploadExpirationPeriod(tusExpirationPeriod)
			// 쓰레드 로컬 캐쉬 활성화
			.withThreadLocalCache(true)
			// 업로드 엔드포인트로 사용할 URI
			.withUploadUri(contextPath + "/api/data/tus");
	}
}
