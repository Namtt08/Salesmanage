package org.project.manage.services;

import org.project.manage.entities.User;
import org.project.manage.request.CartAddRequest;
import org.project.manage.response.CartResponse;
import org.project.manage.response.ProductCartResponse;

public interface OrderProductService {

	CartResponse addCart(CartAddRequest request, User user);

	ProductCartResponse changeProductCart(CartAddRequest request, User user);

	ProductCartResponse deleteProductCart(CartAddRequest request, User user);

	CartResponse getCart(User user);

}
