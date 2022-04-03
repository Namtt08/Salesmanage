package org.project.manage.services.impl;

import org.project.manage.entities.SystemSetting;
import org.project.manage.repository.SystemSettingRepository;
import org.project.manage.services.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SystemSettingServiceImpl implements SystemSettingService {

	@Autowired
	private SystemSettingRepository systemSettingRepository;

	@Override
	public SystemSetting findByCode(String otpBlock) {
		return systemSettingRepository.findByCode(otpBlock).orElse(null);
	}

}
