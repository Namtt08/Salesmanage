package org.project.manage.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class OrderProductDto {

	private Long productId;
	private String productName;
	private Long price;
	private Long totalProduct;
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private String bannerPath;
	
	
	
}
