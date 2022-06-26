package org.project.manage.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionSearchDto {


	private Long id;
	
	private String promotionType;
	
	private String promotionName;
	
	private String promotionCode;
	
	private Long productCategoryId;
	
	private String description;
	
}
