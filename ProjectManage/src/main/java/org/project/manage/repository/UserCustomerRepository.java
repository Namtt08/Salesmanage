package org.project.manage.repository;

import java.util.Optional;

import org.project.manage.entities.User;
import org.project.manage.entities.UserCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCustomerRepository extends JpaRepository<UserCustomer, Long> {

	Optional<UserCustomer> findByCuid(String cuid);

	Optional<UserCustomer> findByPhoneNumber(String phoneNumber);

	Boolean existsByPhoneNumber(String phoneNumber);

	Boolean existsByCuid(String cuid);
}
