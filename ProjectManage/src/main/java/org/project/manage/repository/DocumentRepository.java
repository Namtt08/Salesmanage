package org.project.manage.repository;

import java.util.List;

import org.project.manage.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

	List<Document> findByDocInfoId(Long id);

	List<Document> findByDocTypeAndDocInfoId(String docType, Long id);

	List<Document> findByDocType(String docType);
	
	List<Document> findByDocTypeAndUserId(String docType, Long userId);

}
