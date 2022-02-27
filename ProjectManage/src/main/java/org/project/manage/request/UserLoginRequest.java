package org.project.manage.request;

import lombok.Data;

@Data
public class UserLoginRequest {
	
	private String phonenumber;
	private String cuid;
	private String deviceId;
	private String deviceName;
	private String platform;
	private String osVersion;
	
}
