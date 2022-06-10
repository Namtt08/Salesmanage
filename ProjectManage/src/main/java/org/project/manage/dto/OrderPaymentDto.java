package org.project.manage.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPaymentDto {

	private String orderId;
	private String codeOrders;
	private String status;
	private Long partnerId;
	private String partnerName;
	private Long totalAmount;
	private Long totalDiscount;
	private String createdDate;
	private String paymentMethod;
	private String paymentStatus;
	private String promotionName;
	private String promotionValue;
	private String deliveryAddress;
	private List<OrderProductDto> product;
	private List<OrderProductHistoryDto> history;
	
}
