package org.project.manage.response;

import java.util.List;

import org.project.manage.dto.ProductCategoryDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductCategoryResponse extends MessageSuccessResponse {
	private List<ProductCategoryDto> listCategory;
	
	public ProductCategoryResponse(List<ProductCategoryDto> listCategory) {
		this.listCategory = listCategory;
	}

}
