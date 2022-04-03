package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "product")
@Data
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String productType;
	private String productBrands;
	private Date insuranceDate;
	private String code;
	private String serial;
	private String lotNumber;
	private String chassisNumber;
	private String licensePlate;
	private String saleStatus;
	private String userId;
	private Date createdDate;
	private String productName;
	private Long productCategoryId;
	private Long totalProduct;
	private Date startDate;
	private Date endDate;
	private String status;
	private String salesType;
	private Long price;

}
