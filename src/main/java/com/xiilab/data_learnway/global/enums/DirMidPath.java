package com.xiilab.data_learnway.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DirMidPath {
	// 가공할 이미지를 저장하는 디렉토리 경로
	UPLOAD_IMG_MID_PATH("/uploadImg"),
	TUS_META_MID_PATH("/tus/meta"),
	TUS_META_LOCKS_PATH("/tus/meta/locks"),
	TUS_META_UPLOADS_PATH("/tus/meta/uploads");

	private final String path;
}
