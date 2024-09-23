package com.xiilab.data_learnway.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DirRootPath {
	ROOT_FILE_PATH_WINDOW("C/:Users/xiilab/Desktop/data_learnway"),
	ROOT_FILE_PATH_LINUX("/usr/local/uploadFile/data_learnway");

	private final String path;
}
