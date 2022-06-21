package org.project.manage.controller;

import java.util.List;

import org.project.manage.dto.ProductCategoryDto;
import org.project.manage.entities.User;
import org.project.manage.exception.AppException;
import org.project.manage.request.ProductListRequest;
import org.project.manage.response.ApiResponse;
import org.project.manage.response.ListProductRespose;
import org.project.manage.response.ProductCategoryResponse;
import org.project.manage.response.ProductDetailResponse;
import org.project.manage.services.ProductService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/product")
@Slf4j
public class ProductController {

	@Autowired
	private SuccessHandler successHandler;

	@Autowired
	private ErrorHandler errorHandler;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;

	@PostMapping("/get-product")
	public ApiResponse getProduct(@RequestBody ProductListRequest request) {
		long start = System.currentTimeMillis();
		try {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findByUsername(name)
					.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));
			ListProductRespose response = this.productService.getListProduct(request, user); 
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#getProduct#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}

	@GetMapping("/get-product-category")
	public ApiResponse getProductCategory() {
		long start = System.currentTimeMillis();
		try {
			List<ProductCategoryDto> listProductCategory = productService.getAllProductCategory();
			return this.successHandler.handlerSuccess(new ProductCategoryResponse(listProductCategory), start);
		} catch (Exception e) {
			log.error("#getProductCategory#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}
	
	@GetMapping("/get-product-detail")
	public ApiResponse getProductDetail(@RequestParam(value = "id", required = true) Long id) {
		long start = System.currentTimeMillis();
		try {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findByUsername(name)
					.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));
			ProductDetailResponse response = this.productService.getProductDetail(id, user); 
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#getProductDetail#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}
	
	
}
