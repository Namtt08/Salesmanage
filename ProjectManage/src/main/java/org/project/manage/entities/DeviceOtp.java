package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "device_otp")

public class DeviceOtp {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String deviceId;
	private String deviceName;
	private String platform;
	private String osVersion;
	private Date createdDate;
	
	public DeviceOtp() {
	}
}
