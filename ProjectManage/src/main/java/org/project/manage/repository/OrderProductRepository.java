package org.project.manage.repository;

import java.util.List;
import java.util.Optional;

import org.project.manage.entities.CartTemp;
import org.project.manage.entities.OrderProduct;
import org.project.manage.entities.TransactionProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
