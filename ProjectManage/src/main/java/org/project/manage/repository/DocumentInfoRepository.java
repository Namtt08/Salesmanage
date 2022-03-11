package org.project.manage.repository;

import java.util.List;
import java.util.Optional;

import org.project.manage.entities.Document;
import org.project.manage.entities.DocumentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentInfoRepository extends JpaRepository<DocumentInfo, Long> {

	Optional<DocumentInfo> findByDocTypeAndUserIdAndDeletedDateIsNull(String docType, Long id);

}
