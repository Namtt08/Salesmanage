package org.project.manage.services;

import java.util.List;

import org.project.manage.dto.ProductCategoryDto;
import org.project.manage.entities.User;
import org.project.manage.request.ProductListRequest;
import org.project.manage.response.GetAllProductNameListRespone;
import org.project.manage.response.ListProductRespose;
import org.project.manage.response.ProductDetailResponse;

public interface ProductService {

	List<ProductCategoryDto> getAllProductCategory();

	ListProductRespose getListProduct(ProductListRequest request, User user);

	ProductDetailResponse getProductDetail(Long id, User user);
	
	GetAllProductNameListRespone getAllProductName();

}
