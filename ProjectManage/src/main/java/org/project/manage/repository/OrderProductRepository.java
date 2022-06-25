package org.project.manage.repository;

import java.util.List;
import java.util.Optional;

import org.project.manage.entities.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

	Optional<OrderProduct> findByCodeOrders(String orderCode);

	List<OrderProduct> findByUuidId(String orderId);

	//List<OrderProduct> findByUserIdByCreatedDateDesc(Long id);

}
