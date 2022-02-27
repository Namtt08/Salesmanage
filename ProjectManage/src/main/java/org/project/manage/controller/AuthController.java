package org.project.manage.controller;

import java.util.Date;

import org.project.manage.entities.CustomerLoginHistory;
import org.project.manage.entities.User;
import org.project.manage.repository.UserRepository;
import org.project.manage.request.OtpLoginRequest;
import org.project.manage.request.UserLoginRequest;
import org.project.manage.response.BaseResponse;
import org.project.manage.response.LoginResponse;
import org.project.manage.response.LoginView;
import org.project.manage.security.JwtUtils;
import org.project.manage.services.CustomerLoginHistoryService;
import org.project.manage.services.UserService;
import org.project.manage.util.AppConstants;
import org.project.manage.util.AppResultCode;
import org.project.manage.util.MessageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private UserRepository userRepository;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerLoginHistoryService customerLoginHistoryService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/otp")
	public ResponseEntity<?> saveOtpLogin(@RequestBody OtpLoginRequest otpLoginRequest) {
		
		return null;
		
	}
	
	@PostMapping("/authentication")
	public ResponseEntity<?> authentication(@RequestBody UserLoginRequest userLoginRequest) {
		User userCustomer = userService.findByCuid(userLoginRequest.getPhonenumber()).orElse(null);
		if (userCustomer == null) {
			userCustomer = userService.createUserCustomer(userLoginRequest);
		}
		if (userCustomer.isBlockUser()) {
			return ResponseEntity.ok(new BaseResponse(AppResultCode.AS_NOT_FOUND_RECORD, MessageResult.GRD001_BLOCK));
		}
		
		String jwt = jwtUtils.generateJwtToken(userLoginRequest.getCuid());
		
		this.saveCustomerLoginHistory(userCustomer, userLoginRequest);
		
		return ResponseEntity.ok(new LoginResponse(AppResultCode.AS_SUCCESS, MessageResult.SUCCESS,
				new LoginView(userCustomer.getCuid(), userCustomer.getPhoneNumber(), jwt,
						userCustomer.getEmail(), userCustomer.isBlockUser(), userCustomer.getNationalId(),
						userCustomer.getGender(), userCustomer.getFullName())));
	}

	private void saveCustomerLoginHistory(User userCustomer, UserLoginRequest userLoginRequest) {
		CustomerLoginHistory cusHis = new CustomerLoginHistory();
		cusHis.setCreatedDate(new Date());
		cusHis.setUserId(userCustomer.getId());
		cusHis.setDeviceId(userLoginRequest.getDeviceId());
		cusHis.setDeviceName(userLoginRequest.getDeviceName());
		cusHis.setPlatform(userLoginRequest.getPlatform());
		customerLoginHistoryService.save(cusHis);
	}
	
}
