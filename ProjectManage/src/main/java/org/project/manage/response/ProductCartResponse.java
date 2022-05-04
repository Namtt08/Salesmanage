package org.project.manage.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductCartResponse extends MessageSuccessResponse {

	private Long id;
	private Long productId;
	private String productName;
	private Long price;
	private Long partnerId;
	private String partnerName;
	private Long totalProduct;
	private String messageProduct;
}
