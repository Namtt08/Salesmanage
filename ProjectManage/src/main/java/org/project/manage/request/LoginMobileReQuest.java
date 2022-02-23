package org.project.manage.request;

import lombok.Data;

@Data
public class LoginMobileReQuest {
	private String ciud;
	private String phoneNumber;
	private String deviceId;
	private String deviceName;
	private String platform;
}
