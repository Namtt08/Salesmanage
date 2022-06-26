package org.project.manage.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusProducDto {

	private Long productId;

	private String productName;

	@JsonInclude(JsonInclude.Include.ALWAYS)
	private Long price;

	@JsonInclude(JsonInclude.Include.ALWAYS)
	private Long  totalProduct;

	@JsonInclude(JsonInclude.Include.ALWAYS)
	private Long priceAfterPromotion;

	@JsonInclude(JsonInclude.Include.ALWAYS)
	private Long orderStatus;
	
	private String codeOrders;

	@JsonInclude(JsonInclude.Include.ALWAYS)
	private Long partnerId;

	private String uuidId;

	@JsonInclude(JsonInclude.Include.ALWAYS)
	private Long totalAmount;

	private String createdBy;

	private String createdDate;

	private int paymentStatus;

	private String paymentMethod;

	@JsonInclude(JsonInclude.Include.ALWAYS)
	private Long voucherId;

	@JsonInclude(JsonInclude.Include.ALWAYS)
	private Long promotionId;

	@JsonInclude(JsonInclude.Include.ALWAYS)
	private Long totalDiscount;

	private String deliveryAddress;

	private String note;
	
	private String voucherName;
	
	private String promotionName;
	
	private String paymentName;
	
	private String bannerPath;

	public OrderStatusProducDto() {
		
	}

	public OrderStatusProducDto(Long productId, String productName, Long price, Long totalProduct,
			Long priceAfterPromotion, Long orderStatus, String codeOrders, Long partnerId, String uuidId,
			Long totalAmount, String createdBy, String createdDate, int paymentStatus, String paymentMethod,
			Long voucherId, Long promotionId, Long totalDiscount, String deliveryAddress, String note ) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.totalProduct = totalProduct;
		this.priceAfterPromotion = priceAfterPromotion;
		this.orderStatus = orderStatus;
		this.codeOrders = codeOrders;
		this.partnerId = partnerId;
		this.uuidId = uuidId;
		this.totalAmount = totalAmount;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.paymentStatus = paymentStatus;
		this.paymentMethod = paymentMethod;
		this.voucherId = voucherId;
		this.promotionId = promotionId;
		this.totalDiscount = totalDiscount;
		this.deliveryAddress = deliveryAddress;
		this.note = note;
		this.voucherName = voucherName;
		this.promotionName = promotionName;
	}

}
