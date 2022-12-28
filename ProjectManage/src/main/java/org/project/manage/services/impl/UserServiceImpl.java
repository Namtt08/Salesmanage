package org.project.manage.services.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.project.manage.dto.GaraInfoDto;
import org.project.manage.dto.PresenterRequestDto;
import org.project.manage.entities.*;
import org.project.manage.enums.ChargeTypeEnum;
import org.project.manage.exception.AppException;
import org.project.manage.repository.*;
import org.project.manage.request.*;
import org.project.manage.response.*;
import org.project.manage.security.ERole;
import org.project.manage.services.UserService;
import org.project.manage.util.AppConstants;
import org.project.manage.util.ContentType;
import org.project.manage.util.DateHelper;
import org.project.manage.util.FileStorageProperties;
import org.project.manage.util.MessageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

	@Autowired
	private DocumentInfoRepository documentInfoRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private FileStorageProperties fileStorageProperties;

	@Autowired
	private DocumentTypeRepository documentTypeRepository;

	@Autowired
	private PaymentHistoryRepository paymentHistoryRepository;
	
	@Autowired
	private UserIntroducedRepository  userIntroducedRepository;
	
	@Autowired
	private UserNotificationRepository userNotificationRepository;
	
	@Autowired
	private UserLeasedRepository userLeasedRepository;
	
	@Autowired
	private DocumentContractRepository  documentContractRepository;

	@Autowired
	private GaraRepository garaRepository;

	private static final String  LEASE_CONTRACT = "LEASE_CONTRACT";

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

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
					.orElseThrow(() -> new AppException(MessageResult.GRD002_ROLE));
			roles.add(userRole);
			User user = new User();
			user.setUsername(userLoginRequest.getPhonenumber());
			user.setPassword(passwordEncoder.encode(userLoginRequest.getCuid()));
			user.setPhoneNumber(userLoginRequest.getPhonenumber());
			user.setCuid(userLoginRequest.getCuid());
			user.setBlockUser(false);
			user.setUserType(AppConstants.USER_CUSTOMER);
			user.setCreatedDate(new Date());

			user.setRoles(roles);
			userRepository.save(user);
			return user;
		} catch (Exception e) {
			log.error("#createUserCustomer#ERROR#" + e.getMessage());
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

	@Override
	public void updateUserInfo(UpdateUserInfo userInfoRq, User user) throws Exception {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.DATE_FORMAT);
			Date date = new Date();

			if (StringUtils.isNotBlank(userInfoRq.getNationalId())) {
				user.setNationalId(userInfoRq.getNationalId());
			}
			if (StringUtils.isNotBlank(userInfoRq.getPhoneNumber2())) {
				user.setPhoneNumber2(userInfoRq.getPhoneNumber2());
			}
			if (StringUtils.isNotBlank(userInfoRq.getFullName())) {
				user.setFullName(userInfoRq.getFullName());
			}
			if (StringUtils.isNotBlank(userInfoRq.getEmail())) {
				user.setEmail(userInfoRq.getEmail());
			}
			if (StringUtils.isNotBlank(userInfoRq.getGender())) {
				user.setGender(userInfoRq.getGender());
			}
			if (StringUtils.isNotBlank(userInfoRq.getDob())) {
				user.setDob(formatter.parse(userInfoRq.getDob()));
			}
			user.setModifiedDate(date);
			if (StringUtils.isNotBlank(userInfoRq.getAvatarContent())) {
				byte[] decodedBytes = Base64.decodeBase64(userInfoRq.getAvatarContent());
				Tika tika = new Tika();
				String contentType = tika.detect(decodedBytes);
				String extension = ContentType.getExtension(contentType);
				String fileName = "AVATAR" + "_" + UUID.randomUUID() + "." + extension;
				String filePath = user.getPhoneNumber() + "/" + fileName;
				Path fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

				Path targetLocation = fileStorageLocation.resolve(filePath);
				FileUtils.writeByteArrayToFile(targetLocation.toFile(), decodedBytes);
				user.setAvatar(fileStorageProperties.getUploadDir() + "/" + filePath);
			}
			userRepository.save(user);
		} catch (Exception e) {
			log.error("#updateUserInfo#ERROR#:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}

	private DocumentInfo saveOrUpdate(UpdateUserInfo userInfoRq, User user) throws ParseException {
		DocumentInfo documentInfo = this.documentInfoRepository.findByUserIdAndDeletedDateIsNull(user.getId())
				.orElse(null);
		if (documentInfo == null) {
			documentInfo = new DocumentInfo();
			documentInfo = this.convertDocumentInfoDtoToEntity(documentInfo, userInfoRq);
			documentInfo.setUserId(user.getId());
			documentInfo.setCreatedBy(user.getUsername());
			documentInfo.setCreatedDate(new Date());
			;
			documentInfo = documentInfoRepository.save(documentInfo);
		} else {
			documentInfo = this.convertDocumentInfoDtoToEntity(documentInfo, userInfoRq);
			documentInfo.setModifiedDate(new Date());
			documentInfo = documentInfoRepository.save(documentInfo);
		}
		return documentInfo;
	}

	private DocumentInfo convertDocumentInfoDtoToEntity(DocumentInfo info, UpdateUserInfo userInfoRq)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.DATE_FORMAT);
		if (StringUtils.isNotBlank(userInfoRq.getDrivingLicense())) {
			info.setDrivingLicense(userInfoRq.getDrivingLicense());
		}

		if (StringUtils.isNotBlank(userInfoRq.getVehicleModel())) {
			info.setVehicleModel(userInfoRq.getVehicleModel());
		}

		if (StringUtils.isNotBlank(userInfoRq.getVehicleYearProd())) {
			info.setVehicleYearProd(userInfoRq.getVehicleYearProd());
		}

		if (StringUtils.isNotBlank(userInfoRq.getVehicleSit())) {
			info.setVehicleSit(userInfoRq.getVehicleSit());
		}

		if (StringUtils.isNotBlank(userInfoRq.getVehicleIssueDate())) {
			info.setVehicleIssueDate(formatter.parse(userInfoRq.getVehicleIssueDate()));
		}

		if (StringUtils.isNotBlank(userInfoRq.getVehicleExpiryDate())) {
			info.setVehicleExpiryDate(formatter.parse(userInfoRq.getVehicleExpiryDate()));
		}

		if (StringUtils.isNotBlank(userInfoRq.getVehicleType())) {
			info.setVehicleType(userInfoRq.getVehicleType());
		}

		if (StringUtils.isNotBlank(userInfoRq.getCavetNumber())) {
			info.setCavetNumber(userInfoRq.getCavetNumber());
		}

		if (StringUtils.isNotBlank(userInfoRq.getInsuranceExpiryDate())) {
			info.setInsuranceExpiryDate(formatter.parse(userInfoRq.getInsuranceExpiryDate()));
		}

		if (userInfoRq.getInsuranceFee() != null) {
			info.setInsuranceFee(userInfoRq.getInsuranceFee());
		}

		if (StringUtils.isNotBlank(userInfoRq.getInsuranceCompany())) {
			info.setInsuranceCompany(userInfoRq.getInsuranceCompany());
		}

		if (StringUtils.isNotBlank(userInfoRq.getCivilInsuranceExpDate())) {
			info.setCivilInsuranceExpDate(formatter.parse(userInfoRq.getCivilInsuranceExpDate()));
		}

		if (userInfoRq.getCivilInsuranceFee() != null) {
			info.setCivilInsuranceFee(userInfoRq.getCivilInsuranceFee());
		}

		if (StringUtils.isNotBlank(userInfoRq.getCivilInsuranceCompany())) {
			info.setCivilInsuranceCompany(userInfoRq.getCivilInsuranceCompany());
		}

		if (StringUtils.isNotBlank(userInfoRq.getCooperativeDate())) {
			info.setCooperativeDate(formatter.parse(userInfoRq.getCooperativeDate()));
		}

		if (StringUtils.isNotBlank(userInfoRq.getCooperativeDueDate())) {
			info.setCooperativeDueDate(userInfoRq.getCooperativeDueDate());
		}

		if (StringUtils.isNotBlank(userInfoRq.getCooperativeContractPeriod())) {
			info.setCooperativeContractPeriod(userInfoRq.getCooperativeContractPeriod());
		}

		if (StringUtils.isNotBlank(userInfoRq.getCooperativeContractDate())) {
			info.setCooperativeContractDate(formatter.parse(userInfoRq.getCooperativeContractDate()));
		}

		if (StringUtils.isNotBlank(userInfoRq.getCooperativeContractExpDate())) {
			info.setCooperativeContractExpDate(formatter.parse(userInfoRq.getCooperativeContractExpDate()));
		}

		if (StringUtils.isNotBlank(userInfoRq.getOther())) {
			info.setOther(userInfoRq.getOther());
		}

		return info;
	}

	@Override
	public void updateDocumentInfo(UpdateUserInfo userInfoRq, User user) throws Exception {
		try {
			DocumentInfo documentInfo = this.saveOrUpdate(userInfoRq, user);
			if (userInfoRq.getDocuments() != null && !userInfoRq.getDocuments().isEmpty()) {
				for (DocumentRequest docRQ : userInfoRq.getDocuments()) {
					List<Document> documentList = this.documentRepository.findByDocTypeAndDocInfoId(docRQ.getDocType(),
							documentInfo.getId());
					if (documentList != null) {
						for (Document document2 : documentList) {
							try {
								Files.delete(Paths.get(document2.getDocPath()));
							} catch (Exception e) {
								log.error("deleteDoc:" + e.getMessage());
							}
							documentRepository.delete(document2);
						}
					}
					if (docRQ.getFiles() != null && !docRQ.getFiles().isEmpty()) {
						for (FileContentRequest fileContentRequest : docRQ.getFiles()) {
							Document document = new Document();
							byte[] decodedBytes = Base64.decodeBase64(fileContentRequest.getFileContent());
							String extension = FilenameUtils.getExtension(docRQ.getFileName());

							if (StringUtils.isEmpty(extension)) {
								Tika tika = new Tika();
								String contentType = tika.detect(decodedBytes);
								extension = ContentType.getExtension(contentType);
							}
							String fileName = docRQ.getDocType() + "_" + UUID.randomUUID() + "." + extension;
							String filePath = user.getPhoneNumber() + "/" + fileName;
							Path fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath()
									.normalize();

							Path targetLocation = fileStorageLocation.resolve(filePath);
							FileUtils.writeByteArrayToFile(targetLocation.toFile(), decodedBytes);
							document.setDocPath(fileStorageProperties.getUploadDir() + "/" + filePath);
							document.setDocInfoId(documentInfo.getId());
							document.setDocType(docRQ.getDocType());
							document.setUserId(user.getId());
							document.setCreatedDate(new Date());
							documentRepository.save(document);
						}
					}
				}

			}
		} catch (Exception e) {
			log.error("#updateDocumentInfo#ERROR#:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public DocumentInfoResponse getDocumentInfo(User user) {
		DocumentInfoResponse response = new DocumentInfoResponse();
		DocumentInfo documentInfo = this.documentInfoRepository.findByUserIdAndDeletedDateIsNull(user.getId())
				.orElse(null);
		if (documentInfo == null) {
			List<DocumentType> documentTypeList = documentTypeRepository.findAll();
			List<DocumentResponse> docResponse = new ArrayList<>();
			for (DocumentType documentType : documentTypeList) {
				DocumentResponse documentResponse = new DocumentResponse();
				documentResponse.setDocType(documentType.getDocType());
				documentResponse.setDocName(documentType.getDocName());
				documentResponse.setTotalDoc(documentType.getTotalDoc());
				documentResponse.setUploadType(documentType.getUploadType());
				docResponse.add(documentResponse);
			}
			response.setDocuments(docResponse);
			return response;
		}
		response = this.convertDocumentInfoEntityToDto(documentInfo);
		List<DocumentType> documentTypeList = documentTypeRepository.findAll();
		List<DocumentResponse> docResponse = new ArrayList<>();
		for (DocumentType documentType : documentTypeList) {
			DocumentResponse documentResponse = new DocumentResponse();
			documentResponse.setDocType(documentType.getDocType());
			documentResponse.setDocName(documentType.getDocName());
			documentResponse.setTotalDoc(documentType.getTotalDoc());
			documentResponse.setUploadType(documentType.getUploadType());
			List<Document> documentList = this.documentRepository.findByDocTypeAndDocInfoId(documentType.getDocType(),
					documentInfo.getId());
			if (documentList != null && !documentList.isEmpty()) {
				List<FilePathRespone> listFiles = new ArrayList<>();
				for (Document document : documentList) {
					FilePathRespone filePath = new FilePathRespone();
					filePath.setFilePath(document.getDocPath());
					listFiles.add(filePath);
				}
				documentResponse.setPaths(listFiles);

			}
			docResponse.add(documentResponse);
		}
		response.setDocuments(docResponse);
		return response;
	}

	private DocumentInfoResponse convertDocumentInfoEntityToDto(DocumentInfo documentInfo) {
		DocumentInfoResponse info = new DocumentInfoResponse();
		SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.DATE_FORMAT);
		if (StringUtils.isNotBlank(documentInfo.getDrivingLicense())) {
			info.setDrivingLicense(documentInfo.getDrivingLicense());
		}

		if (StringUtils.isNotBlank(documentInfo.getVehicleModel())) {
			info.setVehicleModel(documentInfo.getVehicleModel());
		}

		if (StringUtils.isNotBlank(documentInfo.getVehicleYearProd())) {
			info.setVehicleYearProd(documentInfo.getVehicleYearProd());
		}

		if (StringUtils.isNotBlank(documentInfo.getVehicleSit())) {
			info.setVehicleSit(documentInfo.getVehicleSit());
		}

		if (documentInfo.getVehicleIssueDate() != null) {
			info.setVehicleIssueDate(formatter.format(documentInfo.getVehicleIssueDate()));
		}

		if (documentInfo.getVehicleExpiryDate() != null) {
			info.setVehicleExpiryDate(formatter.format(documentInfo.getVehicleExpiryDate()));
		}

		if (StringUtils.isNotBlank(documentInfo.getVehicleType())) {
			info.setVehicleType(documentInfo.getVehicleType());
		}

		if (StringUtils.isNotBlank(documentInfo.getCavetNumber())) {
			info.setCavetNumber(documentInfo.getCavetNumber());
		}

		if (documentInfo.getInsuranceExpiryDate() != null) {
			info.setInsuranceExpiryDate(formatter.format(documentInfo.getInsuranceExpiryDate()));
		}

		if (documentInfo.getInsuranceFee() != null) {
			info.setInsuranceFee(documentInfo.getInsuranceFee());
		}

		if (StringUtils.isNotBlank(documentInfo.getInsuranceCompany())) {
			info.setInsuranceCompany(documentInfo.getInsuranceCompany());
		}

		if (documentInfo.getCivilInsuranceExpDate() != null) {
			info.setCivilInsuranceExpDate(formatter.format(documentInfo.getCivilInsuranceExpDate()));
		}

		if (documentInfo.getCivilInsuranceFee() != null) {
			info.setCivilInsuranceFee(documentInfo.getCivilInsuranceFee());
		}

		if (StringUtils.isNotBlank(documentInfo.getCivilInsuranceCompany())) {
			info.setCivilInsuranceCompany(documentInfo.getCivilInsuranceCompany());
		}

		if (documentInfo.getCooperativeDate() != null) {
			info.setCooperativeDate(formatter.format(documentInfo.getCooperativeDate()));
		}

		if (StringUtils.isNotBlank(documentInfo.getCooperativeDueDate())) {
			info.setCooperativeDueDate(documentInfo.getCooperativeDueDate());
		}

		if (StringUtils.isNotBlank(documentInfo.getCooperativeContractPeriod())) {
			info.setCooperativeContractPeriod(documentInfo.getCooperativeContractPeriod());
		}

		if (documentInfo.getCooperativeContractDate() != null) {
			info.setCooperativeContractDate(formatter.format(documentInfo.getCooperativeContractDate()));
		}

		if (documentInfo.getCooperativeContractExpDate() != null) {
			info.setCooperativeContractExpDate(formatter.format(documentInfo.getCooperativeContractExpDate()));
		}

		if (StringUtils.isNotBlank(documentInfo.getOther())) {
			info.setOther(documentInfo.getOther());
		}

		return info;
	}

	@Override
	public Optional<User> findByUsername(String username) {

		return userRepository.findByUsername(username);
	}

	@Override
	public Optional<User> findById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id);
	}

	@Override
	public List<PaymentHistoryDto> getHistoryPayment(User user) {
		List<PaymentHistoryDto> listResponse = paymentHistoryRepository
				.findByUserIdOrderByCreatedDateDesc(user.getId()).stream().map(x -> {
					PaymentHistoryDto dto = new PaymentHistoryDto();
					dto.setAmount(x.getAmount());
					dto.setCodeOrders(x.getCodeOrders());
					dto.setDescription(x.getDescription().isEmpty()? ChargeTypeEnum.getByValue(x.getChargeType()).getName(): x.getDescription());
					dto.setCreatedDate(DateHelper.convertDateTime(x.getCreatedDate()));
					dto.setChargeType(Integer.toString(x.getChargeType()));
					dto.setChargeName(ChargeTypeEnum.getByValue(x.getChargeType()).getName());
					return dto;
				}).collect(Collectors.toList());
		return listResponse;
	}

	@Override
	public PresenterResponse addPresenter(User user,PresenterRequestDto presenterRequestDto) {
		
		PresenterResponse response = new PresenterResponse();
		UserIntroducedEntity userIntroducedEntity = new UserIntroducedEntity();
		userIntroducedEntity.setCreatedBy(user.getUsername());
		userIntroducedEntity.setCreatedDate(new Date());
		userIntroducedEntity.setType(presenterRequestDto.getType());
		userIntroducedEntity.setUserIntroducedId(presenterRequestDto.getUserIntroduceId());
		userIntroducedEntity.setUserId(user.getId());
		userIntroducedRepository.save(userIntroducedEntity);
		return response;
	}

	@Override
	public NotificationDetailResponse getNotificationDetail(User user) {
		NotificationDetailResponse response = new NotificationDetailResponse ();
		
		List<UserNotificationEntity> listData= userNotificationRepository.getAllNotificationByUserId(user.getId());
		if(!Objects.isNull(listData)) {
			response.setListNoti(listData);	
		}		
		return response;
	}
	
	@Override
	public DocumentContractResponse getDocumentContract(User user) {
		DocumentContractResponse response = new DocumentContractResponse();
		List<UserLeasedEntity> UserLeasedEntity = userLeasedRepository.findByUserId(user.getId());
		Optional<DocumentType> docTypeInfo = documentTypeRepository.findByDocType(LEASE_CONTRACT);
		List<DocumentResponse> docResponse = new ArrayList<>();
		for (UserLeasedEntity userLeasedEntityTemp : UserLeasedEntity) {
			if (docTypeInfo.isPresent()) {
				DocumentType docTypeDto = docTypeInfo.get();
				DocumentResponse documentResponse = new DocumentResponse();
				documentResponse.setDocType(docTypeDto.getDocType());
				documentResponse.setDocName(docTypeDto.getDocName());
				documentResponse.setTotalDoc(docTypeDto.getTotalDoc());
				documentResponse.setUploadType(docTypeDto.getUploadType());
				documentResponse.setContractCode(userLeasedEntityTemp.getContractCode());

				List<DocumentContractEntity> documentContractList = documentContractRepository
						.getListPathDocConttract(userLeasedEntityTemp.getId(), LEASE_CONTRACT,
								userLeasedEntityTemp.getContractCode());
				if (documentContractList != null && !documentContractList.isEmpty()) {
					List<FilePathRespone> listFiles = new ArrayList<>();
					for (DocumentContractEntity temp : documentContractList) {
						FilePathRespone filePath = new FilePathRespone();
						filePath.setFilePath(temp.getDocPath());
						listFiles.add(filePath);
					}
					documentResponse.setPaths(listFiles);
			}
				docResponse.add(documentResponse);
		}


	
	}

		response.setDocuments(docResponse);
		return response;
	}

	@Override
	public UserUpdateNotificationResponse updateNotification(User user, String actionStatus, Long userNotificationId) {
		UserUpdateNotificationResponse response =new UserUpdateNotificationResponse();
		
		
		Optional<UserNotificationEntity> UserNotificationEntityOptional = userNotificationRepository.findById(userNotificationId);
		if(UserNotificationEntityOptional.isPresent()) {
			UserNotificationEntity  userNotificationEntity= UserNotificationEntityOptional.get();
			if(StringUtils.equalsIgnoreCase("0", actionStatus)) {// 0 da xem: 1: xoa no 
				userNotificationEntity.setSeen(true);
			}else {
				userNotificationEntity.setDeleteDate(new Date());
			}
			
			userNotificationRepository.save(userNotificationEntity);
			
		}

		return response;
	}

	@Override
	public UpdateTokenResponse updateToken(User user, String token) {
		UpdateTokenResponse  response =  new UpdateTokenResponse();
		Optional<User> userOptional = userRepository.findById(user.getId());
		User users = userOptional.get();
		users.setTokenFirebase(token);
		userRepository.save(users);
		return response;
	}

	@Override
	public void testNoti(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AccountDeleteResponse deleteAccount(User user) {
		AccountDeleteResponse  response = new AccountDeleteResponse();
		Optional<User> userOptional = userRepository.findById(user.getId());
		User users = userOptional.get();
		users.setBlockUser(true);
		users.setDeleteDate(new Date());
		users.setDeleteBy(user.getUsername());
		userRepository.save(users);
		
		return response;
	}

	@Override
	public WalletHistoryResponse walletHistory(User user) {
		WalletHistoryResponse response = new WalletHistoryResponse();
		
//		List<PaymentHistory> getHistoryPaymentByUserId(Long userId);
		return response;
	}

	@Override
	public UserPaymentlResponse PayingForGarageService(User user, UserPaymentRequest request) {
		UserPaymentlResponse response = new UserPaymentlResponse();
		if(request.getAmount()==null){
			request.setAmount(0L);
		}
		Optional<User> userGaraOptional = userRepository.getUserDetailById(request.getGaraId(),false);
		Optional<User> userPaymentOptional = userRepository.getUserDetailById(user.getId(),false);
		if(userGaraOptional.isPresent()) {
			if(StringUtils.equalsIgnoreCase("ROLE_GARA", userGaraOptional.get().getUserType())){
				User userPayment =userPaymentOptional.get();
				User userGara =userGaraOptional.get();
				if(userPayment.getPoint() == null||userPayment.getPoint()<request.getAmount()){
					userPayment.setPoint(0L);
					response.setCodeStatus(99999);
					response.setMessageStatus("Tài khoản của bạn không đủ để thực hiện giao dịch, vui lòng liên hệ admin để nạp thêm tiền vào ví");
					return response;
				}else {
					userPayment.setPoint(userPayment.getPoint() - request.getAmount());
					userRepository.save(userPayment);
					userGara.setPoint(userGara.getPoint() + request.getAmount());
					userRepository.save(userGara);
					response.setCodeStatus(0);
					response.setMessageStatus("Giao dịch thành công!");

				}

			}else {
				response.setCodeStatus(99999);
				response.setMessageStatus("Đây không phải tài khoản GARA, Bạn không thể thanh toán cho tài khoản này!");
				return response;
			}
		}else {
			response.setCodeStatus(99999);
			response.setMessageStatus("Không tìm thấy thông tin Gara");
			return response;
		}
		return response;
	}

}
