package org.project.manage.repository;

import java.util.List;

import org.project.manage.entities.UserLeasedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLeasedRepository extends JpaRepository<UserLeasedEntity, Long> {

        List<UserLeasedEntity> findByUserId(Long userId);

	
}
