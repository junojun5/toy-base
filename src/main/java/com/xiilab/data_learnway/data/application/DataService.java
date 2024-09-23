package com.xiilab.data_learnway.data.application;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.xiilab.data_learnway.data.application.dto.request.InsertDataRequest;
import com.xiilab.data_learnway.data.domain.Data;
import com.xiilab.data_learnway.data.domain.repository.DataRepository;
import com.xiilab.data_learnway.data.exception.DataErrorCode;
import com.xiilab.data_learnway.data.infrastructure.CustomGoogleDriveApi;
import com.xiilab.data_learnway.global.enums.DirMidPath;
import com.xiilab.data_learnway.global.exception.RestApiException;
import com.xiilab.data_learnway.global.utils.CustomFileUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import me.desair.tus.server.upload.UploadInfo;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataService {
	private final DataRepository dataRepository;
	private final TusFileUploadService tusFileUploadService;

	public void insertDataByBasic(InsertDataRequest insertDataRequest) {
		MultipartFile[] mfArr = insertDataRequest.getFiles();

		// 업로드할 디렉토리 생성
		String dirPath = DirMidPath.UPLOAD_IMG_MID_PATH.getPath() + "/temp";

		for (MultipartFile multipartFile : mfArr) {
			try {
				saveData(dirPath, multipartFile);
			} catch (IOException e) {
				log.error("IOException Error!", e);
				throw new RestApiException(DataErrorCode.UPLOAD_FAILED_MESSAGE);
			}
		}
	}

	public void insertDataByTus(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 업로드
			tusFileUploadService.process(request, response);

			// 현재 업로드 정보
			UploadInfo uploadInfo = tusFileUploadService.getUploadInfo(request.getRequestURI());

			// 완료 된 경우 파일 저장
			if (uploadInfo != null && !uploadInfo.isUploadInProgress()) {
				// "metadata"로 넘어온 원본 파일명 추출
				String filename = Optional.ofNullable(uploadInfo.getMetadata().get("filename"))
					.orElseThrow(() -> new RestApiException(DataErrorCode.TUS_FILE_NAME_ERROR_MESSAGE));

				// 파일 저장
				// TODO "/uploadImg/temp"는 임시 디렉토리 경로임. 새로 생성되는 디렉토리 경로로 수정필요.
				CustomFileUtils.saveInputStreamToFile("/uploadImg/temp", filename,
					tusFileUploadService.getUploadedBytes(request.getRequestURI()));

				// TODO DB 저장

				// 2. zip 파일 압축해제 모듈 -> DB에 저장 -> 서버에 파일로 저장 (테스트 필요)
				// 임시 파일 삭제
				tusFileUploadService.deleteUpload(request.getRequestURI());
			}
		} catch (IOException | TusException e) {
			log.error("exception was occurred. message={}", e.getMessage(), e);
			throw new RestApiException(DataErrorCode.TUS_UPLOAD_FAILED_MESSAGE);
		}
	}

	public void insertDataByGoogleDrive(String accessToken, String fileIds) {
		String[] driveFileIds = fileIds.split(",");
		CustomGoogleDriveApi customGoogleDriveApi = null;
		try {
			customGoogleDriveApi = new CustomGoogleDriveApi(accessToken, DriveScopes.DRIVE_FILE);
		} catch (IOException e) {
			throw new RestApiException(DataErrorCode.GDRIVE_NOT_FOUND_CREDENTIALS);
		}

		try {
			for (String id : driveFileIds) {
				File findFile = customGoogleDriveApi.getFileById(id);
				// String imageType = modifyMimeType(findFile.getMimeType());

				// 구글 드라이브에 저장된 이미지 파일 Outputstream에 저장
				OutputStream outputStream = new ByteArrayOutputStream();
				customGoogleDriveApi.downloadFileById(id, outputStream);

				byte[] bytes = ((ByteArrayOutputStream)outputStream).toByteArray();

				try {
					String savedFullPath = CustomFileUtils.saveBytesToFile(DirMidPath.UPLOAD_IMG_MID_PATH.getPath(),
						findFile.getName(), bytes);
				} catch (IOException e) {
					throw new RestApiException(DataErrorCode.GDRIVE_FILE_SAVE_FAILED);
				}

				// TODO 파일 저장 후 DB에 저장
			}
		} catch (IOException e) {
			throw new RestApiException(DataErrorCode.GDRIVE_INFO_LOAD_FAILED);
		}
	}

	/**
	 * 구글 드라이브에서 가져온 파일 타입 DB에 적재가능한 타입으로 변환
	 * */
	private String modifyGoogleDriveMimeType(String mimeType) {
		if (!StringUtils.hasText(mimeType)) {
			throw new RestApiException(DataErrorCode.GDRIVE_FILE_TYPE_ERROR);
		}
		return mimeType.contains("image") ? "IMAGE" : "VIDEO";
	}

	private void saveData(String dirPath, MultipartFile multipartFile) throws IOException {
		// 파일 저장 후 저장 경로 가져옴
		String saveFullFilePath = CustomFileUtils.saveMultipartFileToFile(
			dirPath,
			multipartFile);

		// 저장된 파일명
		String saveFileName = saveFullFilePath.substring(saveFullFilePath.lastIndexOf("/") + 1);

		// TODO 이미지 width, height 추출
		// File saveFile = new File(saveFullFilePath);
		// BufferedImage bImg = null;
		// bImg = ImageIO.read(saveFile);

		// Data 저장
		Data saveData = Data.builder()
			.savePath(saveFullFilePath)
			.saveFileName(saveFileName)
			.originFileName(multipartFile.getOriginalFilename())
			.dataSize(multipartFile.getSize())
			.build();
		dataRepository.save(saveData);
	}
}
