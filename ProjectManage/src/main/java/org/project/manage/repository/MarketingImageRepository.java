package org.project.manage.repository;

import java.util.Optional;

import org.project.manage.entities.MarketingImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketingImageRepository extends JpaRepository<MarketingImageEntity, Long> {

	Optional<MarketingImageEntity> findByIdDocImage(Long docId);

}
