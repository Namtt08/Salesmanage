package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "document_info")
@Data
public class DocumentInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String drivingLicense;
	private String vehicleModel;
	private String vehicleYearProd;
	private String vehicleSit;
	private Date vehicleIssueDate;
	private Date vehicleExpiryDate;
	private String vehicleType;
	private String cavetNumber;
	private Date insuranceExpiryDate;
	private Long insuranceFee;
	private String insuranceCompany;
	private Date civilInsuranceExpDate;
	private Long civilInsuranceFee;
	private String civilInsuranceCompany;
	private Date cooperativeDate;
	private Date cooperativeExpDate;
	private String cooperativeDueDate;
	private String cooperativeContractPeriod;
	private Date cooperativeContractDate;
	private Date cooperativeContractExpDate;
	private String other;
	private Long userId;

	private Date createdDate;
	private Date modifiedDate;
	private Date deletedDate;
	private String createdBy;

//	@OneToMany(mappedBy = "documentInfo")
//	private List<Document> documents;

	public DocumentInfo() {
	}
}
