package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "cart_temp")
@Data
public class CartTemp {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long productId;
	private Long totalProduct;
	private Long userId;
	private Long partnerId;
	private String createdBy;
	private Date createdDate;
	private Date modifiedDate;
}
