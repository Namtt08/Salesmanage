package org.project.manage.controller;

import java.util.Date;

import javax.validation.Valid;

import org.project.manage.entities.CustomerLoginHistory;
import org.project.manage.entities.UserCustomer;
import org.project.manage.repository.UserRepository;
import org.project.manage.request.UserLoginRequest;
import org.project.manage.response.BaseResponse;
import org.project.manage.response.LoginResponse;
import org.project.manage.response.LoginView;
import org.project.manage.response.MessageResponse;
import org.project.manage.security.JwtUtils;
import org.project.manage.services.CustomerLoginHistoryService;
import org.project.manage.services.UserCustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	private UserCustomerService userCustomerService;
	
	@Autowired
	private CustomerLoginHistoryService customerLoginHistoryService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/register")
	public ResponseEntity<?> registerUser1(@Valid @RequestBody UserLoginRequest userLoginRequest) {
		UserCustomer userCustomer = userCustomerService.findByPhoneNumber(userLoginRequest.getCuid()).orElse(null);
		if (userCustomer == null) {
			userCustomer = userCustomerService.createUserCustomer(userLoginRequest);
		}
		if (userCustomer.isBlockUser()) {
			return ResponseEntity.ok(new BaseResponse(500, "Bạn đã bị khóa tài khoản vui lòng liên hệ tổng đài viên"));
		}
		
		String jwt = jwtUtils.generateJwtToken(userCustomer.getPhoneNumber());
		userCustomer.setToken(jwt);
		userCustomer.setDeviceId(userLoginRequest.getDeviceId());
		userCustomer.setDeviceName(userLoginRequest.getDeviceName());
		userCustomer.setPlatform(userLoginRequest.getPlatform());
		userCustomer = userCustomerService.save(userCustomer);
		
		this.saveCustomerLoginHistory(userCustomer);
		
		return ResponseEntity.ok(new LoginResponse(200, "Thành công",
				new LoginView(userCustomer.getCuid(), userCustomer.getPhoneNumber(), jwt,
						userCustomer.getEmail(), userCustomer.isBlockUser(), userCustomer.getNationalId(),
						userCustomer.getGender(), userCustomer.getFullName())));
	}

	private void saveCustomerLoginHistory(UserCustomer userCustomer) {
		CustomerLoginHistory cusHis = new CustomerLoginHistory();
		cusHis.setCreatedDate(new Date());
		cusHis.setUserId(userCustomer.getId());
		cusHis.setDeviceId(userCustomer.getDeviceId());
		cusHis.setDeviceName(userCustomer.getDeviceName());
		cusHis.setPlatform(userCustomer.getPlatform());
		customerLoginHistoryService.save(cusHis);
	}
}
