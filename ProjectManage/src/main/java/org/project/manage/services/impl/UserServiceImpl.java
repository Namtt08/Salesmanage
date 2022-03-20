package org.project.manage.services.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.project.manage.entities.Document;
import org.project.manage.entities.DocumentInfo;
import org.project.manage.entities.DocumentType;
import org.project.manage.entities.Role;
import org.project.manage.entities.User;
import org.project.manage.exception.AppException;
import org.project.manage.repository.DocumentInfoRepository;
import org.project.manage.repository.DocumentRepository;
import org.project.manage.repository.DocumentTypeRepository;
import org.project.manage.repository.RoleRepository;
import org.project.manage.repository.UserRepository;
import org.project.manage.request.DocumentRequest;
import org.project.manage.request.FileContentRequest;
import org.project.manage.request.UpdateUserInfo;
import org.project.manage.request.UserLoginRequest;
import org.project.manage.response.DocumentInfoResponse;
import org.project.manage.response.DocumentResponse;
import org.project.manage.response.FilePathRespone;
import org.project.manage.security.ERole;
import org.project.manage.services.UserService;
import org.project.manage.util.AppConstants;
import org.project.manage.util.ContentType;
import org.project.manage.util.FileStorageProperties;
import org.project.manage.util.MessageResult;
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
			userRepository.save(user);
		} catch (Exception e) {
			log.error("updateUserInfo:" + e.getMessage());
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
			log.error("updateDocumentInfo:" + e.getMessage());
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
					filePath.setPaths(document.getDocPath());
					listFiles.add(filePath);
				}
				documentResponse.setFiles(listFiles);

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

}
