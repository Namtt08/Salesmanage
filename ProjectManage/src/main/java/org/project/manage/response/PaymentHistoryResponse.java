package org.project.manage.response;

import lombok.Data;

@Data
public class PaymentHistoryResponse {
	
	private String codeOrders;
	
	private String chargeType;
	
	private Long amount;
	
	private String description;
	
	private String createdDate;
	
	private String chargeName;
	
}
