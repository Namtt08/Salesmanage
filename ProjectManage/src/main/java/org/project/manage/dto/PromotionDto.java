package org.project.manage.dto;

import org.project.manage.entities.Promotion;
import org.project.manage.entities.Voucher;
import org.project.manage.util.SystemConfigUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionDto {

	private Long id;

	private String code;
	
	private String name;
	
	private String description;
	
	private String type;
	
	private String promotionType;
	
	private Long promotionValue;
	
	private Long maxAmount;
	
	private Long promotionTotal;
	
	private String bannerPath;
	
	private String productCateName;

	public PromotionDto(Promotion model) {
		this.id = model.getId();
		this.code = model.getPromotionCode();
		this.name= model.getPromotionName();
		this.description= model.getDescription();
		this.type =SystemConfigUtil.PROMOTION;
		this.promotionType = model.getPromotionType();
		this.promotionValue=model.getPromotionValue();
		this.promotionTotal= model.getPromotionTotal();
		this.maxAmount= model.getMaxAmount();
		this.bannerPath = this.getBannerPath();
		
	}
	
	public PromotionDto(Voucher model) {
		this.id = model.getId();
		this.code = model.getVoucherCode();
		this.name= model.getVoucherName();
		this.description= model.getDescription();
		this.type =SystemConfigUtil.VOURCHER;
		this.promotionValue=model.getPoint();
		this.bannerPath = this.getBannerPath();
	}

	public PromotionDto() {
		
	}
}
