package org.project.manage.services.impl;

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
import org.project.manage.entities.Document;
import org.project.manage.entities.DocumentInfo;
import org.project.manage.entities.Role;
import org.project.manage.entities.User;
import org.project.manage.exception.AppException;
import org.project.manage.repository.DocumentInfoRepository;
import org.project.manage.repository.DocumentRepository;
import org.project.manage.repository.RoleRepository;
import org.project.manage.repository.UserRepository;
import org.project.manage.request.DocumentRequest;
import org.project.manage.request.UpdateUserInfo;
import org.project.manage.request.UserLoginRequest;
import org.project.manage.security.ERole;
import org.project.manage.services.UserService;
import org.project.manage.util.AppConstants;
import org.project.manage.util.ContentType;
import org.project.manage.util.FileStorageProperties;
import org.project.manage.util.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
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
		SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.DATE_FORMAT);
		Date date = new Date();
		try {
			if (AppConstants.USER_INFO.equals(userInfoRq.getTypeUpdate())) {
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
				userRepository.save(user);
			} else if (AppConstants.DOCUMENT_INFO.equals(userInfoRq.getTypeUpdate())) {
				DocumentInfo documentInfo = this.documentInfoRepository
						.findByDocTypeAndUserIdAndDeletedDateIsNull(userInfoRq.getDocType(), user.getId()).orElse(null);
				if (documentInfo == null) {
					DocumentInfo info = this.convertDocumentInfoDtoToEntity(userInfoRq);
					info.setUserId(user.getId());
					info.setCreatedBy(user.getUsername());
					info = documentInfoRepository.save(info);
					if (!userInfoRq.getDocuments().isEmpty()) {
						for (DocumentRequest docRQ : userInfoRq.getDocuments()) {
							Document document = new Document();

							byte[] decodedBytes = Base64.decodeBase64(docRQ.getFileContent());
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
							document.setDocPath(targetLocation.toString());
							document.setDocInfoId(info.getId());
							document.setCreatedDate(new Date());
							documentRepository.save(document);
						}
					}
				} else {
					documentInfo.setDeletedDate(new Date());
					documentInfoRepository.save(documentInfo);
					List<Document> listDocuments = documentRepository.findByDocInfoId(documentInfo.getId());
					listDocuments.forEach(x->{
						x.setDeletedDate(new Date());
					});
					documentRepository.saveAll(listDocuments);
					DocumentInfo info = this.convertDocumentInfoDtoToEntity(userInfoRq);
					info.setUserId(user.getId());
					info.setCreatedBy(user.getUsername());
					info = documentInfoRepository.save(info);
					
					
					if (!userInfoRq.getDocuments().isEmpty()) {
						for (DocumentRequest docRQ : userInfoRq.getDocuments()) {
							Document document = new Document();

							byte[] decodedBytes = Base64.decodeBase64(docRQ.getFileContent());
							String extension = FilenameUtils.getExtension(docRQ.getFileName());

							if (StringUtils.isEmpty(extension)) {
								Tika tika = new Tika();
								String contentType = tika.detect(decodedBytes);
								extension = ContentType.getExtension(contentType);
							}
							String fileName = info.getDocType() + "_" + UUID.randomUUID() + "." + extension;
							String filePath = user.getPhoneNumber() + "/" + fileName;
							Path fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath()
									.normalize();

							Path targetLocation = fileStorageLocation.resolve(filePath);
							FileUtils.writeByteArrayToFile(targetLocation.toFile(), decodedBytes);
							document.setDocPath(fileStorageProperties.getUploadDir()+"/"+filePath);
							document.setDocInfoId(info.getId());
							document.setCreatedDate(new Date());
							documentRepository.save(document);
						}
					}
				}

			}
		} catch (Exception e) {
			log.error("updateUserInfo:" + e.getMessage());
			throw e;
		}

	}

	private DocumentInfo convertDocumentInfoDtoToEntity(UpdateUserInfo userInfoRq) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.DATE_FORMAT);
		Date date = new Date();
		DocumentInfo info = new DocumentInfo();
		info.setIdCard(userInfoRq.getIdCard());
		info.setDrivingLicense(userInfoRq.getDrivingLicense());
		info.setVehicleModel(userInfoRq.getVehicleModel());
		info.setVehicleYearProd(userInfoRq.getVehicleYearProd());
		info.setVehicleSit(userInfoRq.getVehicleSit());
		if (StringUtils.isNotBlank(userInfoRq.getVehicleIssueDate())) {
			info.setVehicleIssueDate(formatter.parse(userInfoRq.getVehicleIssueDate()));
		}
		if (StringUtils.isNotBlank(userInfoRq.getVehecleExpiryDate())) {
			info.setVehicleExpiryDate(formatter.parse(userInfoRq.getVehecleExpiryDate()));
		}
		
		info.setVehicleType(userInfoRq.getVehecleType());
		info.setCavetNumber(userInfoRq.getCavetNumber());
		if (StringUtils.isNotBlank(userInfoRq.getInsuranceExpiryDate())) {
			info.setInsuranceExpiryDate(formatter.parse(userInfoRq.getInsuranceExpiryDate()));
		}
		
		info.setInsuranceFee(userInfoRq.getInsuranceFee());
		info.setInsuranceCompany(userInfoRq.getInsuranceCompany());
		if (StringUtils.isNotBlank(userInfoRq.getCivilInsuranceExpDate())) {
			info.setCivilInsuranceExpDate(formatter.parse(userInfoRq.getCivilInsuranceExpDate()));
		}
		
		info.setCivilInsuranceFee(userInfoRq.getCivilInsuranceFee());
		info.setCivilInsuranceCompany(userInfoRq.getCivilInsuranceCompany());
		if (StringUtils.isNotBlank(userInfoRq.getCooperativeDate())) {
			info.setCooperativeDate(formatter.parse(userInfoRq.getCooperativeDate()));
		}
		
		info.setCooperativeDueDate(userInfoRq.getCooperativeDueDate());
		if (StringUtils.isNotBlank(userInfoRq.getCooperativeContractDate())) {
			info.setCooperativeContractDate(formatter.parse(userInfoRq.getCooperativeContractDate()));
		}
		
		if (StringUtils.isNotBlank(userInfoRq.getCooperativeContractExpDate())) {
			info.setCooperativeContractExpDate(formatter.parse(userInfoRq.getCooperativeContractExpDate()));
		}
		
		
		info.setOther(userInfoRq.getOther());
		info.setDocType(userInfoRq.getDocType());
		info.setDocName(userInfoRq.getDocName());
		info.setCreatedDate(date);
		return info;
	}

}
