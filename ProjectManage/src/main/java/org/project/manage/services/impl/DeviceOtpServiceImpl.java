package org.project.manage.services.impl;

import java.util.Date;
import java.util.List;

import org.project.manage.entities.DeviceOtp;
import org.project.manage.repository.DeviceOtpRepository;
import org.project.manage.request.OtpLoginRequest;
import org.project.manage.services.DeviceOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class DeviceOtpServiceImpl implements DeviceOtpService {
	
	@Autowired
	private DeviceOtpRepository deviceOtpRepository;
	
	@Override
	public List<DeviceOtp> findByDeviceIdAndCreatedDate(String deviceId, String fORMAT_DATE) {
		// TODO Auto-generated method stub
		return deviceOtpRepository.findByDeviceIdAndCreatedDate(deviceId, fORMAT_DATE);
	}

	@Override
	public DeviceOtp save(OtpLoginRequest otpLoginRequest) {
		DeviceOtp deviceOtp = new DeviceOtp();
		deviceOtp.setDeviceId(otpLoginRequest.getDeviceId());
		deviceOtp.setDeviceName(otpLoginRequest.getDeviceName());
		deviceOtp.setOsVersion(otpLoginRequest.getOsVersion());
		deviceOtp.setPlatform(otpLoginRequest.getPlatform());
		deviceOtp.setCreatedDate(new Date());
		deviceOtpRepository.save(deviceOtp);
		return deviceOtp;
	}

}
