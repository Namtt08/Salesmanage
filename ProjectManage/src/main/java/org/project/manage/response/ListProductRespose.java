package org.project.manage.response;

import java.util.List;

import org.project.manage.dto.ProductDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ListProductRespose extends MessageSuccessResponse {
	private int total;
	private List<ProductDto> listProduct;
	
	public ListProductRespose (int total, List<ProductDto> listProduct) {
		this.total = total;
		this.listProduct = listProduct;
	}

}
