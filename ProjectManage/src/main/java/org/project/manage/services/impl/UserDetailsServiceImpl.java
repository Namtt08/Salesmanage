package org.project.manage.services.impl;

import javax.transaction.Transactional;

import org.project.manage.entities.User;
import org.project.manage.repository.UserRepository;
import org.project.manage.security.UserDetailsImpl;
import org.project.manage.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
		User user = userRepository.findByPhoneNumberAndUserType(phoneNumber, AppConstants.USER_CUSTOMER)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with phoneNumber: " + phoneNumber));
		return UserDetailsImpl.build(user);
		
	}
	
}
