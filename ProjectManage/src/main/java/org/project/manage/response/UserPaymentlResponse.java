package org.project.manage.response;

import lombok.Data;
import org.project.manage.dto.GaraInfoDto;

@Data
public class UserPaymentlResponse extends MessageSuccessResponse {
	
	private String timeTransaction;

}
