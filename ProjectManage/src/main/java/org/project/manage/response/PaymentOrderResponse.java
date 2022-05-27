package org.project.manage.response;

import lombok.Data;

@Data
public class PaymentOrderResponse extends MessageSuccessResponse {

	private String orderId;
	
}
