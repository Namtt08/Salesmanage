package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "payment_history")
@Data
public class PaymentHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long userId;
	
	private String codeOrders;
	
	private int chargeType;
	
	private Long amount;
	
	private String description;
	
	private Date createdDate;
	
	private String createdBy;

	private Long discountService;

	private Long garaId;
	

}
