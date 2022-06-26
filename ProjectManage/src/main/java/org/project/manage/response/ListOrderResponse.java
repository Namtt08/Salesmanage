package org.project.manage.response;

import java.util.List;

import org.project.manage.dto.OrderPaymentDto;
import org.project.manage.dto.OrderStatusProducDto;

import lombok.Data;

@Data
public class ListOrderResponse extends MessageSuccessResponse {

	//List<OrderPaymentDto> listOrder;
	
	List<OrderStatusProducDto> listOrder;
	
}
