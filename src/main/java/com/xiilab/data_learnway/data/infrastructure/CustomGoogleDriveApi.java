package com.xiilab.data_learnway.data.infrastructure;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

public class CustomGoogleDriveApi {
	private final Drive service;

	public CustomGoogleDriveApi(String accessToken, String driveScopes) throws IOException {
		this.service = initAuthorizedApiClientService(accessToken, driveScopes);
	}

	/**
	 * 액세스 토큰으로 구글API 호출할 수 있는 클라이언트 서비스 생성
	 * */
	private Drive initAuthorizedApiClientService(String accessToken, String driveScopes) throws IOException {
		// 사전 인증된 사용자 자격 증명을 로드 후 Google Cloud 서비스에 대한 인증 정보를 적용
		GoogleCredentials credentials = null;
		//  Google Cloud 서비스 계정의 사전 인증 정보를 자동으로 찾아 구성
		credentials = GoogleCredentials.getApplicationDefault()
			/**
			 * 인증된 사용자의 권한 scope을 설정
			 * 구글 드라이브 read, update, delete 권한 부여
			 * */
			.createScoped(Arrays.asList(driveScopes));

		HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
			credentials);

		return new Drive.Builder(new NetHttpTransport(),
			GsonFactory.getDefaultInstance(),
			requestInitializer)
			// 프론트에서 넘겨받은 OAuth2 액세스 토큰 설정
			.setHttpRequestInitializer(
				reqeust -> {
					reqeust.getHeaders().setAuthorization("Bearer " + accessToken);
				})
			.setApplicationName("Data Studio")
			.build();
	}

	/**
	 * 드라이브에 저장된 모든 IMAGE 파일 가져오기
	 * */
	public List<File> getListImageFiles() throws IOException {
		return service.files().list().setQ("mimeType='image/jpeg' or mimeType='image/png'")
			.setFields("*").execute().getFiles();
	}

	/**
	 * 구글 드라이브 파일ID로 드라이브에 저장된 파일 정보 가져오기
	 * */
	public File getFileById(String fileId) throws IOException {
		return service.files().get(fileId)
			.execute();
	}

	/**
	 * 구글 드라이브 파일ID로 드라이브에 저장된 파일 다운로드
	 * */
	public void downloadFileById(String fileId, OutputStream outputStream) throws IOException {
		service.files().get(fileId)
			.executeMediaAndDownloadTo(outputStream);
	}
}
