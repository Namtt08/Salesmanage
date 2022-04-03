package org.project.manage.response;

import java.util.List;

import org.project.manage.dto.ProductDto;

import lombok.Data;

@Data
public class ListProductRespose extends MessageSuccessResponse {
	private int total;
	private List<ProductDto> listProduct;
	
	public ListProductRespose (int total, List<ProductDto> listProduct) {
		this.total = total;
		this.listProduct = listProduct;
	}

}
