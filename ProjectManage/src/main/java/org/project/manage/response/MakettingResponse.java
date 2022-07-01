package org.project.manage.response;

import java.util.List;

import org.project.manage.dto.DocMarketingDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MakettingResponse extends MessageSuccessResponse {
	
	private List <DocMarketingDto> listDocMarketing;
	
}
