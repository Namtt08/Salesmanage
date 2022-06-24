package org.project.manage.response;

import java.util.List;

import org.project.manage.dto.PromotionDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PromotionProductOrderResponse extends MessageSuccessResponse {

	List<PromotionDto> listPromotion;
	
}
