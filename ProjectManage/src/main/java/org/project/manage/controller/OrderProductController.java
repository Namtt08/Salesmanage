package org.project.manage.controller;

import org.project.manage.entities.User;
import org.project.manage.exception.AppException;
import org.project.manage.request.CartAddRequest;
import org.project.manage.response.ApiResponse;
import org.project.manage.response.CartResponse;
import org.project.manage.response.ProductCartResponse;
import org.project.manage.services.OrderProductService;
import org.project.manage.services.UserService;
import org.project.manage.util.ErrorHandler;
import org.project.manage.util.MessageResult;
import org.project.manage.util.SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderProductController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SuccessHandler successHandler;

	@Autowired
	private ErrorHandler errorHandler;
	
	@Autowired
	private OrderProductService orderProductService;

	@PostMapping("/add-cart")
	public ApiResponse addCart(@RequestBody CartAddRequest request) {
		long start = System.currentTimeMillis();
		try {
			User user = getUserFromAuthentication();
			CartResponse response = orderProductService.addCart(request, user);
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#addCart#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}
	
	
	@PostMapping("/change-product-cart")
	public ApiResponse changeProductCart(@RequestBody CartAddRequest request) {
		long start = System.currentTimeMillis();
		try {
			User user = getUserFromAuthentication();
			ProductCartResponse response = orderProductService.changeProductCart(request, user);
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#addProductCart#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}
	
	@PostMapping("/delete-product-cart")
	public ApiResponse deleteProductCart(@RequestBody CartAddRequest request) {
		long start = System.currentTimeMillis();
		try {
			User user = getUserFromAuthentication();
			ProductCartResponse response = orderProductService.deleteProductCart(request, user);
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#deleteProductCart#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}
	
	@GetMapping("/get-cart")
	public ApiResponse getCart() {
		long start = System.currentTimeMillis();
		try {
			User user = getUserFromAuthentication();
			CartResponse response = orderProductService.getCart(user);
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#deleteProductCart#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}
	
	private User getUserFromAuthentication() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		return userService.findByUsername(name)
				.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));
		
	}
}
