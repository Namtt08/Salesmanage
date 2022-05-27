package org.project.manage.util;

import java.time.ZoneId;

public interface AppConstants {

	String USER_CUSTOMER = "USER_CUSTOMER";
	String USER_INFO = "USER_INFO";
	String DOCUMENT_INFO = "DOCUMENT_INFO";

	String DATE_FORMAT = "dd/MM/yyyy";

	String DATETIME_FORMAT_ISO = "yyyy-MM-dd HH:mm:ss";

	String DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

	String TIME_FORMAT = "HH:mm:ss";

	String SPACE = " ";

	String DENY_APPLICATION = "deny_application";
	
	String deviceId = "deviceId";
	String deviceName ="deviceName";
	String platform ="platform";
	String osVersion ="osVersion";
	
	String LANGUAGE_EN = "en";
	String LANGUAGE_VN = "vn";

	String TOKEN = "TOKEN";

	ZoneId VIETNAM_ZONE = ZoneId.of("Vietnam/Hanoi");
	String LEVEL = "1";
	
	String addition = "addition";
	String decrease = "decrease";

	interface Header {

		String ACCEPT_LANGUAGE = "Accept-Language";
	}
}
