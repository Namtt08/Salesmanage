package org.project.manage.repository;

import java.util.Optional;

import org.project.manage.entities.SystemSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {

	Optional<SystemSetting> findByCode(String otpBlock);

}
