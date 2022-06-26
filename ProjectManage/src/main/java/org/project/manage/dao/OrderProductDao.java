package org.project.manage.dao;

import java.util.List;

import org.project.manage.dto.OrderStatusProducDto;

public interface OrderProductDao {

	List<OrderStatusProducDto> getListOrderProductStatus(Long userId, String orderStatus);

	
}
