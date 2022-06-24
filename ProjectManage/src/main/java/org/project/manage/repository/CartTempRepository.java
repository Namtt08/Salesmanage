package org.project.manage.repository;

import java.util.List;
import java.util.Optional;

import org.project.manage.entities.CartTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartTempRepository extends JpaRepository<CartTemp, Long> {

	Optional<CartTemp> findByUserIdAndProductId(Long id, Long productId);

	List<CartTemp> findByUserId(Long id);

	Optional<CartTemp> findByUserIdAndProductIdOrderByIdDesc(Long id, Long productId);
	
	@Modifying
	@Query(value = "delete cart_temp where product_id=?1 and user_id=?2", nativeQuery = true)
	void deleteProductCardTemp(Long productId, Long userId);

}
