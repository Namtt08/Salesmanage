package org.project.manage.repository;

import java.util.List;

import org.project.manage.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findByDeletedByIsNull();	


}
