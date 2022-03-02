package org.project.manage.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.project.manage.entities.Role;
import org.project.manage.entities.User;
import org.project.manage.exception.AppException;
import org.project.manage.repository.RoleRepository;
import org.project.manage.repository.UserRepository;
import org.project.manage.request.UserLoginRequest;
import org.project.manage.security.ERole;
import org.project.manage.services.UserService;
import org.project.manage.util.AppConstants;
import org.project.manage.util.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Optional<User> findByCuid(String cuid) {
		return userRepository.findByCuid(cuid);
	}

	@Override
	public Optional<User> findByPhoneNumber(String phoneNumber) {
		return userRepository.findByPhoneNumber(phoneNumber);
	}

	@Override
	public User createUserCustomer(@Valid UserLoginRequest userLoginRequest) {
		try {
			List<Role> roles = new ArrayList<>();
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException(MessageResult.GRD002_ROLE));
			roles.add(userRole);
			User user = new User();
			user.setUsername(userLoginRequest.getPhonenumber());
			user.setPassword(passwordEncoder.encode(userLoginRequest.getCuid()));
			user.setPhoneNumber(userLoginRequest.getPhonenumber());
			user.setCuid(userLoginRequest.getCuid());
			user.setUserType(AppConstants.USER_CUSTOMER);
			user.setCreatedDate(new Date());
			
			user.setRoles(roles);
			userRepository.save(user);
			
			throw new AppException("sad");
			//return user;
		} catch (Exception e) {
			log.error("createUserCustomer" + e.getMessage());
			throw e;
		}

	}

	@Override
	public User save(User userCustomer) {
		return userRepository.save(userCustomer);
	}

	@Override
	public Optional<User> findByPhoneNumberAndUserType(String phonenumber, String userCustomer) {
		return userRepository.findByPhoneNumberAndUserType(phonenumber, userCustomer);
	}

}
