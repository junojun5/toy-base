package com.xiilab.data_learnway.global.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import com.xiilab.data_learnway.global.enums.DirRootPath;

// @Slf4j
public class CustomFileUtils {
	/**
	 * OS별로 다른 루트 디렉토리 경로 반환
	 * */
	public static String getRootFilePath() {
		String os = System.getProperty("os.name").toLowerCase();
		return os.contains("win") ? DirRootPath.ROOT_FILE_PATH_WINDOW.getPath() :
			DirRootPath.ROOT_FILE_PATH_LINUX.getPath();
	}

	/**
	 * byte 배열의 데이터를 지정된 경로에 파일로 저장
	 *
	 * @param midPath 파일을 저장할 중간 디렉토리 경로(루트 경로 생략 O) ex) /uploadImg/1/1
	 * @param fileName 원본 파일명
	 * @param bytes 저장할 파일의 내용을 담은 byte 배열
	 * @return 파일 저장된 전체 경로 반환
	 * */
	public static String saveBytesToFile(String midPath, String fileName, byte[] bytes) throws IOException {
		// 파일명
		//String saveFilename = getSaveFilename(fileName);
		String saveFileFullPath = getSaveFileFullPath(midPath, fileName);

		// 파일 저장
		FileUtils.writeByteArrayToFile(new File(saveFileFullPath), bytes);

		return saveFileFullPath;
	}

	/**
	 * MultipartFile 데이터를 지정된 경로에 파일로 저장
	 *
	 * @param midPath 파일을 저장할 중간 디렉토리 경로(루트 경로 생략) ex) /uploadImg/1/1
	 * @param multipartFile 업로드할 파일
	 * @return 파일 저장된 전체 경로 반환
	 * */
	public static String saveMultipartFileToFile(String midPath, MultipartFile multipartFile) throws IOException {
		// 파일명
		//String saveFilename = getSaveFilename(multipartFile.getOriginalFilename());
		String saveFileFullPath = getSaveFileFullPath(midPath, multipartFile.getOriginalFilename());

		// 파일 저장
		multipartFile.transferTo(new File(saveFileFullPath));

		return saveFileFullPath;
	}

	/**
	 * InputStream 데이터를 지정된 경로에 파일로 저장
	 *
	 * @param midPath 파일을 저장할 중간 디렉토리 경로(루트 경로 생략) ex) /uploadImg/1/1
	 * @param fileName 원본 파일명
	 * @param inputStream 저장할 InputStream 파일
	 * @return 파일 저장된 전체 경로 반환
	 * */
	public static String saveInputStreamToFile(String midPath, String fileName, InputStream inputStream) throws
		IOException {
		// 파일명
		String saveFileFullPath = getSaveFileFullPath(midPath, fileName);

		// 파일 저장
		FileUtils.copyInputStreamToFile(inputStream, new File(saveFileFullPath));

		return saveFileFullPath;
	}

	private static String getSaveFilename(String fileName) {
		return FilenameUtils.getBaseName(fileName) + "_" + UUID.randomUUID() + "."
			+ FilenameUtils.getExtension(fileName);
	}

	private static String getSaveFileFullPath(String midPath, String fileName) {
		String saveFilename = getSaveFilename(fileName);
		// 디렉토리 생성
		File directory = new File(CustomFileUtils.getRootFilePath() + midPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		return CustomFileUtils.getRootFilePath() + midPath + File.separator + saveFilename;
	}
}
