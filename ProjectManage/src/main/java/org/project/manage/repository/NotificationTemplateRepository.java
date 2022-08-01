package org.project.manage.repository;

import java.util.Optional;

import org.project.manage.entities.NotificationTemplateEntity;
import org.project.manage.entities.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplateEntity, Long> {

	//NotificationTemplateEntity findByNotiType(String type);
	
	Optional<NotificationTemplateEntity> findByNotiType(String orderCode);
}
