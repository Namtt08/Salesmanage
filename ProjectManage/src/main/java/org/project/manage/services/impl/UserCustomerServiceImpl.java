package org.project.manage.services.impl;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.project.manage.entities.UserCustomer;
import org.project.manage.repository.UserCustomerRepository;
import org.project.manage.request.UserLoginRequest;
import org.project.manage.services.UserCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UserCustomerServiceImpl implements UserCustomerService {

	@Autowired
	private UserCustomerRepository userCustomerRepository;

	@Override
	public Optional<UserCustomer> findByCuid(String cuid) {
		return userCustomerRepository.findByCuid(cuid);
	}

	@Override
	public Optional<UserCustomer> findByPhoneNumber(String phoneNumber) {
		return userCustomerRepository.findByPhoneNumber(phoneNumber);
	}

	@Override
	public UserCustomer createUserCustomer(@Valid UserLoginRequest userLoginRequest) {
		UserCustomer uCus = new UserCustomer();
		uCus.setPhoneNumber(userLoginRequest.getPhonenumber());
		uCus.setCuid(userLoginRequest.getCuid());
		uCus.setCreatedDate(new Date());
		userCustomerRepository.save(uCus);
		return uCus;
	}

	@Override
	public UserCustomer save(UserCustomer userCustomer) {
		return userCustomerRepository.save(userCustomer);
	}

}
