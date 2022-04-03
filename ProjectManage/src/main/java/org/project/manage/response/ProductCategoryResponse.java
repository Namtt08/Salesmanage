package org.project.manage.response;

import java.util.List;

import org.project.manage.dto.ProductCategoryDto;
import org.project.manage.util.AppResultCode;
import org.project.manage.util.MessageResult;

import lombok.Data;

@Data
public class ProductCategoryResponse extends MessageSuccessResponse {
	private List<ProductCategoryDto> listCategory;
	
	public ProductCategoryResponse(List<ProductCategoryDto> listCategory) {
		this.listCategory = listCategory;
	}

}
