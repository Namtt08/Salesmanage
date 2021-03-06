package org.project.manage.repository;

import java.util.List;

import org.project.manage.entities.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

	List<PaymentHistory> findByUserIdOrderByCreatedDateDesc(Long id);

}
