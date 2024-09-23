package com.xiilab.data_learnway;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import com.xiilab.data_learnway.global.enums.DirMidPath;
import com.xiilab.data_learnway.global.utils.CustomFileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableAsync
@SpringBootApplication
public class DataLearnwayApplication {

	public static void main(String[] args) {
		createDirectories();
		SpringApplication.run(DataLearnwayApplication.class, args);
	}

	private static void createDirectories() {
		// String[] createDirNameArr = new String[] {FilePath.UPLOAD_IMG_MID_PATH.getPath()};

		String rootImagePath = CustomFileUtils.getRootFilePath();
		DirMidPath[] dirMidPaths = DirMidPath.values();

		// 권한처리 확인
		Arrays.stream(dirMidPaths).forEach(midPath -> {
			File dir = new File(rootImagePath + midPath.getPath());
			if (!dir.exists()) {
				// dir.mkdirs();
				try {
					// System.out.println("Path.of(dir.getPath()): " + Path.of(dir.getPath()));
					Files.createDirectories(Path.of(dir.getPath()));

				} catch (IOException e) {
					log.error("\"{}\" 디렉토리를 생성할 수 있는 권한이 없습니다.\n해당 경로와 일치하는 디렉토리를 직접 생성해주세요.",
						dir.getPath());
					// log"해당 경로에 디렉토리를 생성할 수 있는 권한이 없어 실행이 실패했습니다.\n서버" + dir.getPath() + "와 일치하는 디렉토리를 직접 생성해주세요. "
				}
			}
		});
	}
}
