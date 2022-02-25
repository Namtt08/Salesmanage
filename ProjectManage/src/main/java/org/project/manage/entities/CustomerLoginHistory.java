package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Entity
@Data
@Table(name = "customer_login_histories")

public class CustomerLoginHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long userId;
	private String deviceId;
	private String deviceName;
	private String platform;
	private Date createdDate;
	
	public CustomerLoginHistory() {
	}
}
