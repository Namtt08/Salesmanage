package org.project.manage.response;

import java.util.List;

import org.project.manage.dto.CartDto;
import org.project.manage.dto.PromotionDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CartResponse extends MessageSuccessResponse {

	private Long userId;
	private Long total;
	private List<CartDto> listCart;
	private Long totalAmount;
	private String messageProduct;
	private PromotionDto promotionDto;
	public CartResponse (Long userId,  Long total, List<CartDto> listCart) {
		this.userId = userId;
		this.total = total;
		this.listCart = listCart;
	}
	
	public CartResponse (Long userId, List<CartDto> listCart, Long totalAmount) {
		this.userId = userId;
		this.totalAmount = totalAmount;
		this.listCart = listCart;
	}
}
