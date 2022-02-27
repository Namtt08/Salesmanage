package org.project.manage.services;

import java.util.List;

import org.project.manage.entities.DeviceOtp;
import org.project.manage.request.OtpLoginRequest;

public interface DeviceOtpService {

	public DeviceOtp save(OtpLoginRequest otpLoginRequest);

	public List<DeviceOtp> findByDeviceIdAndCreatedDate(String deviceId, String fORMAT_DATE);

}
