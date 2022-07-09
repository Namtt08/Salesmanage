package org.project.manage.repository;

import java.util.Optional;

import org.project.manage.entities.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
	
	Optional<DocumentType> findByDocType(String docType);

}
