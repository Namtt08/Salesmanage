package org.project.manage.services;

import java.util.Optional;

import javax.validation.Valid;

import org.project.manage.entities.User;
import org.project.manage.request.UserLoginRequest;

public interface UserService {

	public Optional<User> findByCuid(String cuid);
	
	public User createUserCustomer(@Valid UserLoginRequest userLoginRequest);

	public Optional<User> findByPhoneNumber(String cuid);

	public User save(User userCustomer);

}
