package org.project.manage.repository;

import org.project.manage.entities.GaraInfoEntity;
import org.project.manage.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GaraRepository extends JpaRepository<GaraInfoEntity, Long> {

	@Query(value = "SELECT doc_path  from gara_doc where 1=1 and id = (SELECT min(id) from gara_doc where 1=1 and gara_info_id = ?1) and gara_info_id = ?1", nativeQuery = true)
	String getGaraAvatar(Long id);

	Optional<GaraInfoEntity>findByIdAndDeleteByIsNull(Long Id);

	Optional<GaraInfoEntity>findByUserIdAndDeleteByIsNull(Long userId);
}
