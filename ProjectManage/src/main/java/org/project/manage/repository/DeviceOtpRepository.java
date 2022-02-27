package org.project.manage.repository;

import java.util.List;

import org.project.manage.entities.DeviceOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceOtpRepository extends JpaRepository<DeviceOtp, Long> {

	@Query(value = "select * from device_otp a where FORMAT(a.created_date,'dd/MM/yyyy') = ?2 AND a.device_id = ?1", nativeQuery = true)
	List<DeviceOtp> findByDeviceIdAndCreatedDate(String deviceId, String date);

}
