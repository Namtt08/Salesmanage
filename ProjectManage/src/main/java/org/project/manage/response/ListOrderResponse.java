package org.project.manage.response;

import java.util.List;

import org.project.manage.dto.OrderPaymentDto;

import lombok.Data;

@Data
public class ListOrderResponse extends MessageSuccessResponse {

	List<OrderPaymentDto> listOrder;
	
}
