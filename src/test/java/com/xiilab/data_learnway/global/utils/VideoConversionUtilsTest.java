package com.xiilab.data_learnway.global.utils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;

import groovy.util.logging.Slf4j;

@Slf4j
class VideoConversionUtilsTest {
	@AfterEach
	public void deleteAllImage() {
		try {
			File videoToImageDir = new File(this.getClass().getClassLoader().getResource("videotoimage").getPath());
			Collection<File> files = FileUtils.listFiles(videoToImageDir, new String[] {"png"}, false);

			for (File file : files) {
				FileUtils.delete(file);
			}
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	// @Test
	// @DisplayName("FFMPEG 동영상을 이미지로 변환")
	// void convertVideoToImageFromFfmpeg() throws IOException, InterruptedException {
	// 	String srcVideoFileName = "video/testVideo.mp4";
	// 	ClassLoader classLoader = this.getClass().getClassLoader();
	// 	URL srcVideoUrl = classLoader.getResource(srcVideoFileName);
	//
	// 	assertThat(srcVideoUrl).isNotNull();
	//
	// 	File srcVideoFile = new File(srcVideoUrl.getFile());
	// 	File destDir = new File(classLoader.getResource("videotoimage").getFile());
	//
	// 	VideoConversionUtils videoConversionUtils = new VideoConversionUtils(srcVideoFile, destDir);
	// 	int convertImageCount = videoConversionUtils.convertVideoToImages(30);
	// 	assertThat(convertImageCount).isNotZero();
	// }
}
