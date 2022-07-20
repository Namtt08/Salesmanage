package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "promotion")
@Data
public class Promotion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String promotionCode;
	
	private String promotionName;
	
	private Date startDate;
	
	private Date endDate;

	private Long productCategoryId;

	private String userType;
	
	private String promotionType;
	
	private Long promotionValue;
	
	private Long maxAmount;
	
	private Long promotionTotal;
	
	private String description;

	private Date modifiedDate;

	private String createdBy;

	private Date createdDate;
	
	private String bannerPath;
}
