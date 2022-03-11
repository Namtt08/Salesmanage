package org.project.manage.controller;

import org.project.manage.entities.User;
import org.project.manage.exception.AppException;
import org.project.manage.request.UpdateUserInfo;
import org.project.manage.response.ApiResponse;
import org.project.manage.response.MessageSuccessResponse;
import org.project.manage.services.UserService;
import org.project.manage.util.ErrorHandler;
import org.project.manage.util.SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('ROLE_USER')")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private SuccessHandler successHandler;

	@Autowired
	private ErrorHandler errorHandler;

	@PostMapping("/update-info")
	public ApiResponse updateUserInfo(@RequestBody UpdateUserInfo otpLoginRequest) {
		long start = System.currentTimeMillis();
		try {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findByPhoneNumber(name)
					.orElseThrow(() -> new AppException("Thông tin người dùng không tồn tại"));
			this.userService.updateUserInfo(otpLoginRequest, user);
			return this.successHandler.handlerSuccess(new MessageSuccessResponse(), start);
		} catch (Exception e) {
			log.error("saveOtpLogin:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}
}
