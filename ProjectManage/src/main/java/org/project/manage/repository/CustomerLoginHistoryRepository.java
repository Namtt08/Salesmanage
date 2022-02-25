package org.project.manage.repository;

import org.project.manage.entities.CustomerLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerLoginHistoryRepository extends JpaRepository<CustomerLoginHistory, Long> {

}
