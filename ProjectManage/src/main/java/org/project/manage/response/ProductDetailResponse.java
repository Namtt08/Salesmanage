package org.project.manage.response;

import java.util.List;

import org.project.manage.dto.ProductDocmentDto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductDetailResponse extends MessageSuccessResponse{

	private Long id;
	private String productType;
	private String productBrands;
	private String insuranceDate;
	private String code;
	private String serial;
	private String lotNumber;
	private String chassisNumber;
	private String licensePlate;
	private String saleStatus;
	private String productName;
	private String productCategoryName;
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private Long totalProduct;
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private Long price;
	private String createdDate;
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private List<ProductDocmentDto> productDocuments;

}
