package org.project.manage.response;

import java.util.List;

import org.project.manage.dto.PromotionDto;

import lombok.Data;

@Data
public class PromotionProductOrderResponse extends MessageSuccessResponse {

	List<PromotionDto> listPromotion;
	
}
