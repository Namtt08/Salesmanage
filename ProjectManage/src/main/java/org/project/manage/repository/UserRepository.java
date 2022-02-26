package org.project.manage.repository;

import java.util.Optional;

import org.project.manage.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	Optional<User> findByPhoneNumberAndUserType(String phoneNumber, String userType);

	Optional<User> findByCuid(String cuid);

	Optional<User> findByPhoneNumber(String phoneNumber);
}
