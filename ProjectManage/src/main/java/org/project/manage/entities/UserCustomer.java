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
@Table(name = "users_customer", uniqueConstraints = { @UniqueConstraint(columnNames = "cuid"),
		@UniqueConstraint(columnNames = "phoneNumber") })

public class UserCustomer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String cuid;
	private String phoneNumber;
	private String token;
	private String email;
	private boolean isBlockUser;
	private String nationalId;
	private String fullName;
	private String gender;
	private String avatar;
	private String deviceId;
	private String deviceName;
	private String platform;
	private Date createdDate;
	private Date modifiedDate;
	
	
	public UserCustomer(String cuid, String phoneNumber, String token, String email, String fullName, String nationalId,
			String gender) {
		this.cuid = cuid;
		this.phoneNumber = phoneNumber;
		this.token = token;
		this.email = email;
		this.fullName = fullName;
		this.nationalId = nationalId;
		this.gender = gender;
	}
	
	public UserCustomer() {
	}
}
