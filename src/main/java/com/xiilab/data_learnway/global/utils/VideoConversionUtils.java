package com.xiilab.data_learnway.global.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * 이 유틸리티 클래스 사용하기 위해서는 서버에 ffmpeg 설치가 선행되어야함
 * */
public class VideoConversionUtils {
	// 변환할 비디오 파일
	private final File srcVideoFile;
	// 변환된 비디오 파일이 저장될 디렉토리 명
	private final File destDir;

	public VideoConversionUtils(File srcVideoFile, File destDir) {
		this.srcVideoFile = srcVideoFile;
		this.destDir = destDir;
	}

	/**
	 * 비디오를 이미지로 변환
	 *
	 * @param frame 변환할 특정 프레임 단위 입력
	 * @return 변환된 이미지 개수
	 * */
	public int convertVideoToImages(int frame) throws IOException, InterruptedException {
		// 디렉토리 생성
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		String ogName = FilenameUtils.getBaseName(srcVideoFile.getName());
		String ffmpegCommand =
			"ffmpeg -i " + srcVideoFile.getPath() + " -vf fps=" + frame + " " + destDir + File.separator + ogName
				+ "_%d.png";
		log.info("convertVideoToImage cmd : {}", ffmpegCommand);

		// FFmpeg 실행
		Process process = Runtime.getRuntime().exec(ffmpegCommand);

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}

		// FFmpeg 명령어 실행 완료 대기
		process.waitFor(120, TimeUnit.SECONDS);

		Collection<File> files = FileUtils.listFiles(destDir, new String[] {"png"}, false);

		return files.size();
	}
}
