package org.project.manage.repository;

import java.util.List;
import java.util.Optional;

import org.project.manage.entities.CartTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartTempRepository extends JpaRepository<CartTemp, Long> {

	Optional<CartTemp> findByUserIdAndProductId(Long id, Long productId);

	List<CartTemp> findByUserId(Long id);

	Optional<CartTemp> findByUserIdAndProductIdOrderByIdDesc(Long id, Long productId);
}
