package org.project.manage.repository;

import org.project.manage.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
	
	@Query(value = "SELECT *  FROM product_category pc where 1=1 and pc.id=?1 and pc.status=?2", nativeQuery = true)
	ProductCategory getDataProductCategoryById(Long id, Long status);

}
