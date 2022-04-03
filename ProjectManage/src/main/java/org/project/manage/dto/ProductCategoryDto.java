package org.project.manage.dto;

import org.project.manage.entities.ProductCategory;

import lombok.Data;

@Data
public class ProductCategoryDto {
	
	private Long id;
	private String code;
	private String name;
	
	public ProductCategoryDto(ProductCategory productCategory) {
		this.id =productCategory.getId();
		this.code=productCategory.getCode();
		this.name=productCategory.getName();
	}

}
