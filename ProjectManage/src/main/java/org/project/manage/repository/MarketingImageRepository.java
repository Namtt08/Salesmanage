package org.project.manage.repository;

import java.util.List;

import org.project.manage.entities.MarketingImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MarketingImageRepository extends JpaRepository<MarketingImageEntity, Long> {
	
	
	@Query(value = "select a.* from setting_marketing_info a where a.delete_date is null and status =1", nativeQuery = true)
	List<MarketingImageEntity> getSettingMarketting();

}
