package org.project.manage.services.impl;

import javax.transaction.Transactional;

import org.project.manage.entities.User;
import org.project.manage.entities.UserCustomer;
import org.project.manage.repository.UserCustomerRepository;
import org.project.manage.repository.UserRepository;
import org.project.manage.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService{
	
	@Autowired
	private UserCustomerRepository userCustomerRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
//		User user = userRepository.findByUsername(username)
//				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
//		return UserDetailsImpl.build(user);
		
		UserCustomer user = userCustomerRepository.findByPhoneNumber(phoneNumber)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with phoneNumber: " + phoneNumber));
		return UserDetailsImpl.build(user);
	}
	
}
