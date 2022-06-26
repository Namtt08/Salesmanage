package org.project.manage.repository;

import java.util.List;

import org.project.manage.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

	@Query(nativeQuery = true, value = "SELECT v.* FROM voucher v  where v.user_id =:userId and v.status =1 and SYSDATETIME() BETWEEN v.start_date  AND v.end_date")
	List<Voucher> findByUserId(@Param("userId") Long userId);
	
	Voucher findByUserIdAndVoucherCode(Long userId, String voucherCode);

	
}
