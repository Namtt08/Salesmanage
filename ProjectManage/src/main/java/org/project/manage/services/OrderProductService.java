package org.project.manage.services;

import java.util.List;

import org.project.manage.dto.PromotionDto;
import org.project.manage.entities.User;
import org.project.manage.request.CartAddRequest;
import org.project.manage.response.CartResponse;
import org.project.manage.response.ListProductRespose;
import org.project.manage.response.PaymentOrderResponse;
import org.project.manage.response.ProductCartResponse;
import org.project.manage.response.ProductDetailResponse;

public interface OrderProductService {

	CartResponse addCart(CartAddRequest request, User user);

	ProductCartResponse changeProductCart(CartAddRequest request, User user);

	ProductCartResponse deleteProductCart(CartAddRequest request, User user);

	CartResponse getCart(User user);

	CartResponse getPaymentOrder(CartResponse request, User user);

	PaymentOrderResponse paymentOrder(CartResponse request, User user);

	List<PromotionDto> promotionOrder(CartResponse request, User user);

}
