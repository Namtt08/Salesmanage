package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "order_product_history")
@Data
public class OrderProductHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String status;

	private String codeOrders;
	
	private String paymentMethod;
	
	private Integer paymentStatus;
	
	private String createdBy;

	private Date createdDate;

}
