package org.project.manage.repository;

import java.util.List;

import org.project.manage.entities.UserPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPromotionRepository extends JpaRepository<UserPromotion, Long> {

	List<UserPromotion> findByUserIdAndPromotionId(Long id, Long id2);

}
