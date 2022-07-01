package org.project.manage.repository;

import java.util.Optional;

import org.project.manage.entities.User;
import org.project.manage.entities.UserIntroducedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserIntroducedRepository extends JpaRepository<UserIntroducedEntity, Long> {

	Optional<UserIntroducedEntity> findByUserId(Long userId);


}
