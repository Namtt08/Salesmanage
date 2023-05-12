package org.project.manage.services;

import org.project.manage.dto.GaraInfoDto;
import org.project.manage.entities.User;
import org.project.manage.response.GaraDetailResponse;
import org.project.manage.response.GaraListResponse;

public interface GaraService {

	GaraListResponse getAllGaraInfo();
	GaraDetailResponse getGaraDetail(Long id);


}
