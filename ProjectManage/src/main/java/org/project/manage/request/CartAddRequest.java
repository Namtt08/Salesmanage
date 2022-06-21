package org.project.manage.request;

import lombok.Data;

@Data
public class CartAddRequest {

	private Long id;
	private Long userId;
	private Long productId;
	private Long partnerId;
	private Long totalProduct;
	private String type;
	private String addressDelivery;
	private String note;
}
