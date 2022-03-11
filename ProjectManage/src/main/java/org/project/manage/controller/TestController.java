package org.project.manage.controller;

import org.project.manage.dto.MailDto;
import org.project.manage.response.ApiResponse;
import org.project.manage.response.MessageSuccessResponse;
import org.project.manage.services.EmailService;
import org.project.manage.util.ErrorHandler;
import org.project.manage.util.SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
