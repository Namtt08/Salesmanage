package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "order_product")
@Data
public class OrderProduct {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	private Long partnerId;

	private String status;

	private String uuidId;

	private Long totalAmount;

	private String codeOrders;
	
	private String paymentMethod;
	
	private Integer paymentStatus;
	
	private Long voucherId;
	
	private Long promotionId;

	private String modifiedBy;

	private Date modifiedDate;

	private String createdBy;

	private Date createdDate;
	
	private Long totalDiscount;
	
	private String deliveryAddress;
	
	private String note;
	

}
