package org.project.manage.services.impl;

import javax.transaction.Transactional;

import org.project.manage.entities.User;
import org.project.manage.repository.UserRepository;
import org.project.manage.security.UserDetailsImpl;
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
	public UserDetails loadUserByUsername(String cuid) throws UsernameNotFoundException {
		User user = userRepository.findByCuid(cuid)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with cuid: " + cuid));
		return UserDetailsImpl.build(user);
		
	}
	
}
