package org.project.manage.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class ProductDto {

	private Long id;
	private String productType;
	private String productBrands;
	private String productName;
	private String productCategoryName;
	 @JsonInclude(JsonInclude.Include.ALWAYS)
	private Long totalProduct;
	private String saleStatus;
	 @JsonInclude(JsonInclude.Include.ALWAYS)
	private Long price;
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private String bannerPath;
	private String createdDate;
	private Long partnerId;
	private String partnerName;
	


	// private String insuranceDate;
	// private String code;
	// private String serial;
	// private String lotNumber;
	// private String chassisNumber;
	// private String lisencePlate;
	public ProductDto(Long id, String productType, String productBrands, String productName, String productCategoryName,
			Long totalProduct, String saleStatus,Long price, String bannerPath, String createdDate) {
		this.id = id;
		this.productType = productType;
		this.productBrands = productBrands;
		this.productName = productName;
		this.productCategoryName = productCategoryName;
		this.totalProduct = totalProduct;
		this.saleStatus = saleStatus;
		this.price = price;
		this.bannerPath = bannerPath;
		this.createdDate = createdDate;
	}
	



	public ProductDto(BigDecimal bigDecimal, String productType2, String productBrands2, String productName2,
			String productCategoryName2, BigDecimal bigDecimal2, String saleType2, BigDecimal bigDecimal3,
			String bannerPath2) {
		// TODO Auto-generated constructor stub
	}
	public ProductDto() {
		
	}


}
