package org.project.manage.dto;

import org.project.manage.entities.ProductDocument;

import lombok.Data;

@Data
public class ProductDocmentDto {

	
	private String bannerPath;
	private Long position;
	private String type;
	
	public ProductDocmentDto(ProductDocument productDocument) {
		this.bannerPath = productDocument.getDocPath();
		this.position = productDocument.getPosition();
		this.type = productDocument.getType();
	}
}
