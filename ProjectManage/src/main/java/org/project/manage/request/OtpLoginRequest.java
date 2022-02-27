package org.project.manage.request;

import lombok.Data;

@Data
public class OtpLoginRequest {

	private String deviceId;
	private String deviceName;
	private String platform;
	private String osVersion;
}
