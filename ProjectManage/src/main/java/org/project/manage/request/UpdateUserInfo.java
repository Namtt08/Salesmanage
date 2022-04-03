package org.project.manage.request;

import java.util.List;

import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class UpdateUserInfo {
	
	//user_info
	private String phoneNumber2;
	private String dob;
	@Email
	private String email;
	private String fullName;
	private String gender;
	private String nationalId;
	private String avatarContent;
	
	//document_info
	private String drivingLicense;
	private String vehicleModel;
	private String vehicleYearProd;
	private String vehicleSit;
	private String vehicleIssueDate;
	private String vehicleExpiryDate;
	private String vehicleType;
	private String cavetNumber;
	private String insuranceExpiryDate;
	private Long insuranceFee;
	private String insuranceCompany;
	private String civilInsuranceExpDate;
	private Long civilInsuranceFee;
	private String civilInsuranceCompany;
	private String cooperativeDate;
	private String cooperativeExpDate;
	private String cooperativeDueDate;
	private String cooperativeContractPeriod;
	private String cooperativeContractDate;
	private String cooperativeContractExpDate;
	private String other;
	private List<DocumentRequest> documents;

}
