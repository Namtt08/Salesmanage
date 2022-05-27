package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "transaction_product_order")
@Data
public class TransactionProductOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long productId;

	private Long amount;

	private Long totalProduct;
	
	private String orderProductCode;

	private String modifiedBy;

	private Date modifiedDate;

	private String createdBy;

	private Date createdDate;


}
