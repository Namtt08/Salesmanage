package org.project.manage.repository;

import java.util.List;

import org.project.manage.entities.ProductDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDocumentRepository extends JpaRepository<ProductDocument, Long> {

	List<ProductDocument> findByProductId(Long id);
	
	@Query(value = "SELECT * FROM product_document pd where product_id=?1 and position=?2", nativeQuery = true)
	String getdocPathProductByIdAndPosition(Long id, Long position);

}
