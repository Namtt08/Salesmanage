package org.project.manage.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.project.manage.dto.PresenterRequestDto;
import org.project.manage.entities.User;
import org.project.manage.request.UpdateUserInfo;
import org.project.manage.request.UserLoginRequest;
import org.project.manage.request.UserPaymentRequest;
import org.project.manage.response.*;

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

	public List<PaymentHistoryDto> getHistoryPayment(User user);
	
	PresenterResponse addPresenter(User user, PresenterRequestDto presenterRequestDto);
	
	NotificationDetailResponse getNotificationDetail(User user);
	
	public DocumentContractResponse getDocumentContract(User user);

	UserUpdateNotificationResponse updateNotification(User user, String actionStatus, Long userNotificationId);
	
	UpdateTokenResponse updateToken(User user, String token);
	
	void testNoti(User user);
	
	AccountDeleteResponse deleteAccount(User user);
	
	WalletHistoryResponse walletHistory(User user);

	UserPaymentlResponse PayingForGarageService (User user, UserPaymentRequest request);
}
