package org.project.manage.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.project.manage.entities.User;
import org.project.manage.request.UpdateUserInfo;
import org.project.manage.request.UserLoginRequest;
import org.project.manage.response.DocumentInfoResponse;
import org.project.manage.response.PaymentHistoryResponse;

public interface UserService {

	public Optional<User> findByCuid(String cuid);
	
	public User createUserCustomer(@Valid UserLoginRequest userLoginRequest);

	public Optional<User> findByPhoneNumber(String cuid);

	public User save(User userCustomer);

	public Optional<User> findByPhoneNumberAndUserType(String phonenumber, String userCustomer);

	public void updateUserInfo(UpdateUserInfo otpLoginRequest, User user) throws Exception;

	public void updateDocumentInfo(UpdateUserInfo otpLoginRequest, User user) throws Exception;

	public DocumentInfoResponse getDocumentInfo(User user);
	
	public Optional<User> findByUsername(String username);
	
	public Optional<User> findById(Long id);

	public List<PaymentHistoryResponse> getHistoryPayment(User user);

}
