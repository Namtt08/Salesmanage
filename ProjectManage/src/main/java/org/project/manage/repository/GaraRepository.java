package org.project.manage.repository;

import org.project.manage.entities.GaraInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GaraRepository extends JpaRepository<GaraInfoEntity, Long> {


	
}
