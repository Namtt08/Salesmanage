package org.project.manage.repository;

import java.util.List;

import org.project.manage.entities.OrderProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductHistoryRepository extends JpaRepository<OrderProductHistory, Long> {

	List<OrderProductHistory> findByCodeOrdersOrderByCreatedDateAsc(String codeOrders);

}
