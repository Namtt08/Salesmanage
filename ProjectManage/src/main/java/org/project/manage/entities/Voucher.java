package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "voucher")
@Data
public class Voucher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String voucherCode;
	
	private Long userId;
	
	private Date startDate;
	
	private Date endDate;

	private Long point;

	private boolean status;
	
	private String voucherName;
	
	private String description;
	
	private String bannerPath;
	
	
}
