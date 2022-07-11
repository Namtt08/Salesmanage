package org.project.manage.repository;

import java.util.List;
import java.util.Optional;

import org.project.manage.entities.UserNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotificationEntity, Long> {

	List<UserNotificationEntity> findByUserId(Long userId);
	
	@Query(value = "select a.* from user_notification a where CONVERT(datetime, a.created_date,11)>= DATEADD(day,-7, GETDATE()) AND a.user_id = ?1", nativeQuery = true)
	List<UserNotificationEntity> getAllNotificationByUserId(Long userId);
	
	Optional<UserNotificationEntity> findById(Long id);

}
