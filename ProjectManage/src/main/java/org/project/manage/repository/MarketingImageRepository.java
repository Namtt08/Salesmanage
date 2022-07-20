package org.project.manage.repository;

import java.util.Optional;

import org.project.manage.entities.MarketingImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import antlr.collections.List;

@Repository
public interface MarketingImageRepository extends JpaRepository<MarketingImageEntity, Long> {

}
