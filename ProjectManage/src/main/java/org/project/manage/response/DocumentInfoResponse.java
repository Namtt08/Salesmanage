package org.project.manage.response;

import java.util.List;

import lombok.Data;

@Data
public class DocumentInfoResponse  extends MessageSuccessResponse{

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
	private List<DocumentResponse> documents;
}
