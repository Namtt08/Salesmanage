package org.project.manage.response;

import java.util.List;

import org.project.manage.dto.OrderPaymentDto;
import org.project.manage.dto.OrderStatusProducDto;
import org.project.manage.dto.PromotionSearchDto;
import org.project.manage.entities.Promotion;
import org.project.manage.entities.Voucher;

import lombok.Data;

@Data
public class ListSearchPromotionResponse extends MessageSuccessResponse {

	List<PromotionSearchDto> listPromotion;
	
}
