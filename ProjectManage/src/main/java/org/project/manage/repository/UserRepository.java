package org.project.manage.repository;

import java.util.List;
import java.util.Optional;

import org.project.manage.entities.MarketingImageEntity;
import org.project.manage.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	Optional<User> findByPhoneNumberAndUserType(String phoneNumber, String userType);

	Optional<User> findByCuid(String cuid);

	Optional<User> findByPhoneNumber(String phoneNumber);

	@Query(value = "select a.* from users a where a.delete_date is null and id =?1 and is_block_user = ?2", nativeQuery = true)
	Optional<User> getUserDetailById(Long id, Boolean isBlock);
}
