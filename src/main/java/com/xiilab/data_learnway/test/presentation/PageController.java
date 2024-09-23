package com.xiilab.data_learnway.test.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages")
public class PageController {
	@GetMapping("/google/login")
	public String googleLoginPage() {
		return "pages/oauth2/oauth2Login";
	}

	@GetMapping("/google/drive")
	public String googleDrivePage() {
		return "pages/google/pickerApiSample";
	}

	@GetMapping("/upload/basic")
	public String uploadBasicPage() {
		return "pages/upload/basicFileUpload";
	}

	@GetMapping("/upload/tus")
	public String uploadTusPage() {
		return "pages/upload/tusFileUpload";
	}
}
