package org.project.manage.services;

import org.project.manage.entities.SystemSetting;

public interface SystemSettingService {

	SystemSetting findByCode(String otpBlock);

}
