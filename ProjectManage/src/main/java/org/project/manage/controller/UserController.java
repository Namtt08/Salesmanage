package org.project.manage.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.project.manage.dto.PresenterRequestDto;
import org.project.manage.entities.SystemSetting;
import org.project.manage.entities.User;
import org.project.manage.entities.UserIntroducedEntity;
import org.project.manage.exception.AppException;
import org.project.manage.repository.UserIntroducedRepository;
import org.project.manage.repository.UserRepository;
import org.project.manage.request.UpdateUserInfo;
import org.project.manage.response.AccountDeleteResponse;
import org.project.manage.response.ApiResponse;
import org.project.manage.response.DocumentContractResponse;
import org.project.manage.response.DocumentInfoResponse;
import org.project.manage.response.MessageResponse;
import org.project.manage.response.MessageSuccessResponse;
import org.project.manage.response.NotificationDetailResponse;
import org.project.manage.response.PaymentHistoryResponse;
import org.project.manage.response.PresenterResponse;
import org.project.manage.response.UpdateTokenResponse;
import org.project.manage.response.UserInfoResponse;
import org.project.manage.response.UserUpdateNotificationResponse;
import org.project.manage.services.SystemSettingService;
import org.project.manage.services.UserService;
import org.project.manage.util.AppConstants;
import org.project.manage.util.AppResultCode;
import org.project.manage.util.ErrorHandler;
import org.project.manage.util.MessageResult;
import org.project.manage.util.SuccessHandler;
import org.project.manage.util.SystemConfigUtil;
import org.project.manage.util.SystemSettingConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@Autowired
	private SystemConfigUtil systemConfigUtil;
	
	@Autowired
	private SystemSettingService systemSettingService;
	
	@Autowired
	private UserIntroducedRepository  userIntroducedRepository;

	@PostMapping("/update-user-info")
	public ApiResponse updateUserInfo(@RequestBody UpdateUserInfo otpLoginRequest) {
		long start = System.currentTimeMillis();
		try {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findByUsername(name)
					.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));
			this.userService.updateUserInfo(otpLoginRequest, user);
			return this.successHandler.handlerSuccess(new MessageSuccessResponse(), start);
		} catch (Exception e) {
			log.error("#updateUserInfo#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}

	@PostMapping("/update-document-info")
	public ApiResponse updateDocumentInfo(@RequestBody UpdateUserInfo otpLoginRequest) {
		long start = System.currentTimeMillis();
		try {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findByUsername(name)
					.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));
			this.userService.updateDocumentInfo(otpLoginRequest, user);
			return this.successHandler.handlerSuccess(new MessageSuccessResponse(), start);
		} catch (Exception e) {
			log.error("#updateDocumentInfo#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}

	@GetMapping("/get-user-info")
	public ApiResponse getUserInfo() {
		long start = System.currentTimeMillis();
		try {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findByUsername(name)
					.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));
			String dob = null;
			if (user.getDob() != null) {
				SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.DATE_FORMAT);
				dob = formatter.format(user.getDob());
			}
			SystemSetting systemSetting = systemSettingService.findByCode(SystemConfigUtil.POINT_LV);
			if (systemSetting == null) {
				return successHandler.handlerSuccess(
						new MessageResponse(AppResultCode.AS_ERROR, MessageResult.DATA_NOT_FOUND_CORE), start);
			}
			int lv = (int) ((user.getPoint_lv() == null ? 0 : user.getPoint_lv())
					/ Long.valueOf(systemSetting.getValue()));

			return this.successHandler.handlerSuccess(new UserInfoResponse(user.getCuid(), user.getNationalId(), dob,
					user.getFullName(), user.getPhoneNumber(), user.getEmail(), user.getGender(),
					user.getPhoneNumber2(), user.getAvatar(), lv, user.getPoint() == null ? 0 : user.getPoint()),
					start);
		} catch (Exception e) {
			log.error("#getUserInfo#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}

	@GetMapping("/get-document-info")
	public ApiResponse getDocumentInfo() {
		long start = System.currentTimeMillis();
		try {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findByUsername(name)
					.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));
			DocumentInfoResponse response = userService.getDocumentInfo(user);
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#getDocumentInfo#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}

	@GetMapping("/image/{opt}/{application}/{upload}/{folder}/{fileName:.+}")
	public ResponseEntity<Resource> getImage1(@PathVariable("opt") String opt,
			@PathVariable("application") String application, @PathVariable("upload") String upload,
			@PathVariable("folder") String folder, @PathVariable("fileName") String fileName,
			HttpServletRequest request) {
		String contentType = null;
		String path = File.separator + opt + File.separator + application + File.separator + upload + File.separator
				+ folder + File.separator + fileName;
		Resource resource = loadFileAsResource(path);
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			log.error("getImage1:" + ex.getMessage());
		}
		if (contentType == null) {
			contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
	}

	public Resource loadFileAsResource(String path) {
		try {
			Path targetLocation = Paths.get(path).toAbsolutePath().normalize();
			Resource resource = new UrlResource(targetLocation.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new AppException(MessageResult.GRD005_NOT_FOUND);
			}
		} catch (Exception ex) {
			log.error("#loadFileAsResource#ERROR#:" + ex.getMessage());
			throw new AppException(MessageResult.GRD005_NOT_FOUND);
		}
	}
	
	@GetMapping("/get-history-payment")
	public ApiResponse getHistoryPayment() {
		long start = System.currentTimeMillis();
		try {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findByUsername(name)
					.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));
			List<PaymentHistoryResponse> response = userService.getHistoryPayment(user);
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#getHistoryPayment#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}
	
	@PostMapping("/presenter")
	public ApiResponse addPresenter(@RequestBody PresenterRequestDto presenterRequestDto) {
		long start = System.currentTimeMillis();
		try {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findByUsername(name)
					.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));

			Optional<UserIntroducedEntity> userIntroduced = userIntroducedRepository.findByUserId(user.getId());

			if (userIntroduced.isPresent()) {
				return successHandler
						.handlerSuccess(new MessageResponse(AppResultCode.ERROR, MessageResult.USER_INTRODUCED), start);
			} else {
				PresenterResponse response = userService.addPresenter(user, presenterRequestDto);
				return this.successHandler.handlerSuccess(response, start);
			}
		} catch (Exception e) {
			log.error("#addPresenter#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}
	
	@GetMapping("/notification")
	public ApiResponse allNotification() {
		long start = System.currentTimeMillis();
		try {
			NotificationDetailResponse response = new NotificationDetailResponse();
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findByUsername(name)
					.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));

				 response = userService.getNotificationDetail(user);
				return this.successHandler.handlerSuccess(response, start);

		} catch (Exception e) {
			log.error("#allNotification#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}
	
	@GetMapping("/get-document-contract")
	public ApiResponse getDocumentContract() {
		long start = System.currentTimeMillis();
		try {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findByUsername(name)
					.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));
			DocumentContractResponse response = userService.getDocumentContract(user);
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#getDocumentInfo#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}
	
	@GetMapping("/get-user-point")
	public ApiResponse getUserAmount() {
		long start = System.currentTimeMillis();
		try {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findByUsername(name)
					.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));

			UserInfoResponse response = new UserInfoResponse();
			response.setPoint(user.getPoint());
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#getUserAmount#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}
	
	
	@GetMapping("/update/notification")
	public ApiResponse updateNotification(String actionStatus, Long userNotificationId) {
		long start = System.currentTimeMillis();
		try {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findByUsername(name)
					.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));

			UserUpdateNotificationResponse response = userService.updateNotification(user, actionStatus, userNotificationId);
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#updateNotification#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}
	
	@GetMapping("/update/token")
	public ApiResponse updateToken(String token) {
		long start = System.currentTimeMillis();
		try {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findByUsername(name)
					.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));

			UpdateTokenResponse response = userService.updateToken(user, token);
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#updateToken#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}
	
	@GetMapping("/test/noti")
	public ApiResponse testNoti() {
		long start = System.currentTimeMillis();
		try {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findByUsername(name)
					.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));

			UpdateTokenResponse response =null;
					
					userService.testNoti(user);
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#updateToken#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}
	
	@GetMapping("/delete/account")
	public ApiResponse deleteAccount() {
		long start = System.currentTimeMillis();
		try {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findByUsername(name)
					.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));

			AccountDeleteResponse response =userService.deleteAccount(user);					
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#deleteAccount#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}
}
