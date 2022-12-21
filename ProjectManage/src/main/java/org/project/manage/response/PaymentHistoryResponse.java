package org.project.manage.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PaymentHistoryResponse  extends MessageSuccessResponse{


	private List<PaymentHistoryDto> listPayment;
}
