package org.project.manage.repository;

import java.util.List;

import org.project.manage.entities.ProductDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDocumentRepository extends JpaRepository<ProductDocument, Long> {

	List<ProductDocument> findByProductId(Long id);

}
