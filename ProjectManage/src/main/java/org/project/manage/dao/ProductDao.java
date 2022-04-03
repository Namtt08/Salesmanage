package org.project.manage.dao;

import java.util.List;

import org.project.manage.dto.ProductDto;
import org.project.manage.request.ProductListRequest;

public interface ProductDao {

	List<ProductDto> getListProduct(ProductListRequest request);

	int countListProduct(ProductListRequest request);

	
}
