package org.project.manage.repository;

import java.util.List;

import org.project.manage.entities.Promotion;
import org.project.manage.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

	@Query(nativeQuery = true, value = "SELECT p.* FROM promotion p  where p.product_category_id =:productCategoryId and p.user_type =:userType and p.status =1 and p.delete_by is null and SYSDATETIME() BETWEEN start_date  AND end_date")
	List<Promotion> findByAndProductCategoryIdAndUserType(@Param("productCategoryId")  Long productCategoryId,@Param("userType")  String userType);

	Promotion findByPromotionCode(String promotionCode);
	
}
