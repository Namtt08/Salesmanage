package org.project.manage.repository;

import java.util.List;

import org.project.manage.entities.PaymentHistory;
import org.project.manage.entities.UserNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

	List<PaymentHistory> findByUserIdOrderByCreatedDateDesc(Long id);
	
	@Query(value = "SELECT ph.user_id, \r\n"
			+ "ph.code_orders, \r\n"
			+ "ph.amount,\r\n"
			+ "ph.charge_type,\r\n"
			+ "ph.description\r\n"
			+ "from payment_history ph\r\n"
			+ "where 1=1 AND ph.user_id = ?1 and a.delete_date is null order by ph.created_date desc", nativeQuery = true)
	List<PaymentHistory> getHistoryPaymentByUserId(Long userId);

}
