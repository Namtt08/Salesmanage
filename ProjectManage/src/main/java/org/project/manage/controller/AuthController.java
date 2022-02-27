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
import org.project.manage.response.BaseResponse;
import org.project.manage.response.LoginResponse;
import org.project.manage.response.LoginView;
import org.project.manage.security.JwtUtils;
import org.project.manage.services.CustomerLoginHistoryService;
import org.project.manage.services.DeviceOtpService;
import org.project.manage.services.SystemSettingService;
import org.project.manage.services.UserService;
import org.project.manage.util.AppConstants;
import org.project.manage.util.AppResultCode;
import org.project.manage.util.MessageResult;
import org.project.manage.util.SystemSettingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

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

	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	@PostMapping("/otp")
	public ResponseEntity<?> saveOtpLogin(@RequestBody OtpLoginRequest otpLoginRequest) {
		SystemSetting systemSetting = systemSettingService.findByCode(SystemSettingConstants.OTP_BLOCK);
		if (systemSetting == null) {
			return ResponseEntity
					.ok(new BaseResponse(AppResultCode.AS_DB_ERROR, MessageResult.DATA_NOT_FOUND_CORE));
		}
		SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.DATE_FORMAT);
		Date date = new Date();
		String strDate = formatter.format(date);
		List<DeviceOtp> deviceOtps = deviceOtpService.findByDeviceIdAndCreatedDate(otpLoginRequest.getDeviceId(),
				strDate);
		if (deviceOtps.size() >= Long.valueOf(systemSetting.getValue())) {
			return ResponseEntity
					.ok(new BaseResponse(AppResultCode.AS_ERROR, MessageResult.GRD003_OTP));
		}
		DeviceOtp deviceOtp = deviceOtpService.save(otpLoginRequest);
		return ResponseEntity
				.ok(new BaseResponse(AppResultCode.AS_SUCCESS, MessageResult.SUCCESS, deviceOtp));

	}

	@PostMapping("/authentication")
	public ResponseEntity<?> authentication(@RequestBody UserLoginRequest userLoginRequest) {
		User userCustomer = userService.findByCuid(userLoginRequest.getCuid()).orElse(null);

		if (userCustomer == null) {
			userCustomer = userService.createUserCustomer(userLoginRequest);
		}
		if (userCustomer.isBlockUser()) {
			return ResponseEntity.ok(new BaseResponse(AppResultCode.AS_NOT_FOUND_RECORD, MessageResult.GRD001_BLOCK));
		}

		String jwt = jwtUtils.generateJwtToken(userLoginRequest.getCuid());

		this.saveCustomerLoginHistory(userCustomer, userLoginRequest);

		return ResponseEntity.ok(new LoginResponse(AppResultCode.AS_SUCCESS, MessageResult.SUCCESS,
				new LoginView(userCustomer.getCuid(), userCustomer.getPhoneNumber(), jwt, userCustomer.getEmail(),
						userCustomer.isBlockUser(), userCustomer.getNationalId(), userCustomer.getGender(),
						userCustomer.getFullName())));
	}

	private void saveCustomerLoginHistory(User userCustomer, UserLoginRequest userLoginRequest) {
		CustomerLoginHistory cusHis = new CustomerLoginHistory();
		cusHis.setCreatedDate(new Date());
		cusHis.setUserId(userCustomer.getId());
		cusHis.setDeviceId(userLoginRequest.getDeviceId());
		cusHis.setDeviceName(userLoginRequest.getDeviceName());
		cusHis.setPlatform(userLoginRequest.getPlatform());
		cusHis.setOsVersion(userLoginRequest.getOsVersion());
		customerLoginHistoryService.save(cusHis);
	}

}
