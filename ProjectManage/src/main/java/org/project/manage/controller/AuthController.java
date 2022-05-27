package org.project.manage.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.project.manage.entities.CustomerLoginHistory;
import org.project.manage.entities.DeviceOtp;
import org.project.manage.entities.SystemSetting;
import org.project.manage.entities.User;
import org.project.manage.request.OtpLoginRequest;
import org.project.manage.request.UserLoginRequest;
import org.project.manage.response.ApiResponse;
import org.project.manage.response.LoginView;
import org.project.manage.response.MessageResponse;
import org.project.manage.response.MessageSuccessResponse;
import org.project.manage.security.JwtUtils;
import org.project.manage.services.CustomerLoginHistoryService;
import org.project.manage.services.DeviceOtpService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerLoginHistoryService customerLoginHistoryService;

	@Autowired
	private SystemSettingService systemSettingService;

	@Autowired
	private DeviceOtpService deviceOtpService;

	@Autowired
	private SuccessHandler successHandler;

	@Autowired
	private ErrorHandler errorHandler;

	@PostMapping("/otp")
	public ApiResponse saveOtpLogin(@RequestBody OtpLoginRequest otpLoginRequest) {
		long start = System.currentTimeMillis();
		try {
			SystemSetting systemSetting = systemSettingService.findByCode(SystemConfigUtil.OTP_BLOCK);
			if (systemSetting == null) {
				return successHandler.handlerSuccess(
						new MessageResponse(AppResultCode.AS_ERROR, MessageResult.DATA_NOT_FOUND_CORE), start);
			}
			SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.DATE_FORMAT);
			Date date = new Date();
			String strDate = formatter.format(date);
			List<DeviceOtp> deviceOtps = deviceOtpService.findByDeviceIdAndCreatedDate(otpLoginRequest.getDeviceId(),
					strDate);
			if (deviceOtps.size() >= Long.valueOf(systemSetting.getValue())) {
				return successHandler
						.handlerSuccess(new MessageResponse(AppResultCode.AS_ERROR, MessageResult.GRD003_OTP), start);
			}

			this.deviceOtpService.save(otpLoginRequest);
			return this.successHandler.handlerSuccess(new MessageSuccessResponse(), start);
		} catch (Exception e) {
			log.error("#saveOtpLogin#ERROR:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}

	}

	@PostMapping("/authentication")
	public ApiResponse authentication(@RequestBody UserLoginRequest userLoginRequest) {
		long start = System.currentTimeMillis();
		try {
			User userCustomer = userService.findByCuid(userLoginRequest.getCuid()).orElse(null);

			if (userCustomer == null) {
				userCustomer = userService.createUserCustomer(userLoginRequest);
			}
			if (userCustomer.isBlockUser()) {
				return successHandler.handlerSuccess(
						new MessageResponse(AppResultCode.AS_NOT_FOUND_RECORD, MessageResult.GRD001_BLOCK), start);
			}

			String jwt = jwtUtils.generateJwtToken(userLoginRequest.getCuid());

			this.saveCustomerLoginHistory(userCustomer, userLoginRequest);
			String dob = null;
			if (userCustomer.getDob() != null) {
				SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.DATE_FORMAT);
				dob = formatter.format(userCustomer.getDob());
			}
			return this.successHandler.handlerSuccess(new LoginView(userCustomer.getCuid(),
					userCustomer.getPhoneNumber(), jwt, userCustomer.getEmail(), userCustomer.isBlockUser(),
					userCustomer.getNationalId(), userCustomer.getGender(), userCustomer.getFullName(),userCustomer.getAvatar(), dob, userCustomer.getPhoneNumber2()), start);
		} catch (Exception e) {
			log.error("#authentication#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}

	}

	private void saveCustomerLoginHistory(User userCustomer, UserLoginRequest userLoginRequest) {
		try {
			CustomerLoginHistory cusHis = new CustomerLoginHistory();
			cusHis.setCreatedDate(new Date());
			cusHis.setUserId(userCustomer.getId());
			cusHis.setDeviceId(userLoginRequest.getDeviceId());
			cusHis.setDeviceName(userLoginRequest.getDeviceName());
			cusHis.setPlatform(userLoginRequest.getPlatform());
			cusHis.setOsVersion(userLoginRequest.getOsVersion());
			customerLoginHistoryService.save(cusHis);
		} catch (Exception e) {
			log.error("#saveCustomerLoginHistory#ERROR#:" + e.getMessage());
			e.printStackTrace();
		}

	}
	

}
