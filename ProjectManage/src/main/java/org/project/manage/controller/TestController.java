package org.project.manage.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.project.manage.dto.MailDto;
import org.project.manage.response.ApiResponse;
import org.project.manage.response.MessageSuccessResponse;
import org.project.manage.services.EmailService;
import org.project.manage.util.ErrorHandler;
import org.project.manage.util.SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/test")
@Slf4j
public class TestController {

	@Autowired
	private SuccessHandler successHandler;

	@Autowired
	private ErrorHandler errorHandler;

	@Autowired
	private EmailService emailService;

	protected Path fileStorageLocation;

	@PostMapping("/a")
	public ApiResponse test() {
		long start = System.currentTimeMillis();
		try {
			return this.successHandler.handlerSuccess(new MessageSuccessResponse(), start);
		} catch (Exception e) {
			log.error("saveOtpLogin:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}

	@PostMapping("/send-mail")
	public ApiResponse testSendMail(@RequestBody MailDto mailDto) {
		long start = System.currentTimeMillis();
		try {
			emailService.sendEmail(mailDto);
			return this.successHandler.handlerSuccess(new MessageSuccessResponse(), start);
		} catch (Exception e) {
			log.error("testSendMail:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}

	@GetMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody byte[] getImage(@RequestParam("image") String image) throws IOException {
		File initialFile = new File(image);
		InputStream in = FileUtils.openInputStream(initialFile);
		return IOUtils.toByteArray(in);
	}

	@GetMapping("/image1/{opt}/{application}/{upload}/{folder}/{fileName:.+}")
	public ResponseEntity<Resource> getImage1(@PathVariable("opt") String opt,
			@PathVariable("application") String application, @PathVariable("upload") String upload,
			@PathVariable("folder") String folder, @PathVariable("fileName") String fileName,
			HttpServletRequest request) throws IOException {
		String contentType = null;
		String path = File.separator + opt + File.separator + application + File.separator + upload + File.separator
				+ folder + File.separator + fileName;
		Path targetLocation = Paths.get(path).toAbsolutePath().normalize();
		Resource resource = new UrlResource(targetLocation.toUri());
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			log.error("getImage1:" + ex.getMessage());
		}
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
	}

}
