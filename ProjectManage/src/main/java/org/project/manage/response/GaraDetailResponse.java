package org.project.manage.response;

import lombok.Data;
import org.project.manage.dto.GaraInfoDto;

import java.util.List;

@Data
public class GaraDetailResponse extends MessageSuccessResponse {
	
	private GaraInfoDto garaInfo;

}
