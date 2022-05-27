package org.project.manage.dto;

import lombok.Data;

@Data
public class CartDto {
	private Long id;
	private Long productId;
	private String productName;
	private Long price;
	private Long partnerId;
	private String partnerName;
	private Long totalProduct;
	private String bannerPath;
	private Long cartId;
}
