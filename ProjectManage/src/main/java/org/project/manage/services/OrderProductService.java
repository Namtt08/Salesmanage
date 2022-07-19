package org.project.manage.services;

import org.project.manage.entities.Product;
import org.project.manage.entities.User;
import org.project.manage.request.CartAddRequest;
import org.project.manage.response.CartResponse;
import org.project.manage.response.ListOrderResponse;
import org.project.manage.response.ListSearchPromotionResponse;
import org.project.manage.response.PaymentOrderDetailResponse;
import org.project.manage.response.PaymentOrderResponse;
import org.project.manage.response.ProductCartResponse;
import org.project.manage.response.PromotionProductOrderResponse;

public interface OrderProductService {

	CartResponse addCart(CartAddRequest request, User user);

	ProductCartResponse changeProductCart(CartAddRequest request, User user);

	ProductCartResponse deleteProductCart(CartAddRequest request, User user);

	CartResponse getCart(User user);

	CartResponse getPaymentOrder(CartResponse request, User user);

	PaymentOrderResponse paymentOrder(CartResponse request, User user);

	PromotionProductOrderResponse promotionOrder(CartResponse request, User user);

	PaymentOrderDetailResponse getOrderDetail(String orderId, String orderCode, User user);

	ListOrderResponse getListOrder(User user, String orderStatus);

	String cancelOrder(User user, String orderCode);
	
	ListSearchPromotionResponse getSearchPromotion(User user, String promotionCode);
	
	void sendEmailProduct(String code, Product product);

}
