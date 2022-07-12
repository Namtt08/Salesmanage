package org.project.manage.repository;

import java.util.List;

import org.project.manage.entities.DocumentContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DocumentContractRepository extends JpaRepository<DocumentContractEntity, Long> {
	
	@Query(value = "SELECT * FROM document_contract where user_leased_id=?1 and doc_type=?2 and contract_code=?3", nativeQuery = true)
	List<DocumentContractEntity> getListPathDocConttract(long userLeasedId, String docType, String contractCode);

}