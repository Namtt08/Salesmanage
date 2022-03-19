package org.project.manage.repository;

import java.util.Optional;

import org.project.manage.entities.DocumentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentInfoRepository extends JpaRepository<DocumentInfo, Long> {

	Optional<DocumentInfo> findByUserIdAndDeletedDateIsNull(Long id);

}
