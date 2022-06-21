package org.project.manage.response;

import java.util.List;

import org.project.manage.dto.ProductDocmentDto;
import org.project.manage.dto.ProductDto;
import org.project.manage.dto.PromotionDto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ListProductRespose extends MessageSuccessResponse {
	private int total;
	private List<ProductDto> listProduct;
	private Long totalAmount;
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private List<ProductDocmentDto> productDocuments;
	private List<PromotionDto> listPromotion;
	
	public ListProductRespose (int total, List<ProductDto> listProduct) {
		this.total = total;
		this.listProduct = listProduct;
	}
	
	public ListProductRespose (int total, List<ProductDto> listProduct, Long totalAmount) {
		this.total = total;
		this.listProduct = listProduct;
		this.totalAmount = totalAmount;
	}

	public ListProductRespose (int total, List<ProductDto> listProduct,List<ProductDocmentDto> productDocuments, List<PromotionDto> listPromotion) {
		this.total = total;
		this.listProduct = listProduct;
		this.totalAmount = totalAmount;
	}
}
