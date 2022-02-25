package org.project.manage.services;

import java.util.Optional;

import javax.validation.Valid;

import org.project.manage.entities.UserCustomer;
import org.project.manage.request.UserLoginRequest;

public interface UserCustomerService {

	public Optional<UserCustomer> findByCuid(String cuid);
	
	public UserCustomer createUserCustomer(@Valid UserLoginRequest userLoginRequest);

	public Optional<UserCustomer> findByPhoneNumber(String cuid);

	public UserCustomer save(UserCustomer userCustomer);

}
