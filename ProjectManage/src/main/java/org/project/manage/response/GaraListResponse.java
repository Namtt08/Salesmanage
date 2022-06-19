package org.project.manage.response;

import java.util.List;

import org.project.manage.dto.GaraInfoDto;

import lombok.Data;

@Data
public class GaraListResponse extends MessageSuccessResponse {
	
	private List <GaraInfoDto> listGara;

}
