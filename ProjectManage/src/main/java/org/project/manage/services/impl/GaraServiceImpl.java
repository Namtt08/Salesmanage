package org.project.manage.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.project.manage.dto.GaraInfoDto;
import org.project.manage.entities.GaraInfoEntity;
import org.project.manage.repository.GaraRepository;
import org.project.manage.response.GaraListResponse;
import org.project.manage.services.GaraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GaraServiceImpl implements GaraService {

	@Autowired
	private GaraRepository garaRepository;

	@Override
	public GaraListResponse getAllGaraInfo() {
		GaraListResponse response =  new GaraListResponse();
		List <GaraInfoDto> listGaraInfo = new ArrayList<>() ;
		try {
			List<GaraInfoEntity>  listGara = garaRepository.findAll();
			if(!Objects.isNull(listGara)) {
			for (GaraInfoEntity entity : listGara) {
				GaraInfoDto  dto = new GaraInfoDto();
				dto.setId(entity.getId());
				dto.setGaraCode(entity.getGaraCode());
				dto.setGaraName(entity.getGaraName());
				dto.setPhone(entity.getPhone());
				dto.setLatitude(entity.getLatitude());
				dto.setLongitude(entity.getLongitude());
				dto.setGaraAddress(entity.getGaraAddress());
				dto.setStatus(entity.isStatus());
				dto.setDocPath(entity.getDocPath());
				listGaraInfo.add(dto);
				
			}
			response.setListGara(listGaraInfo);
			}
			return response;
		} catch (Exception e) {
			log.error("#getAllProductCategory#ERROR#:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}


}
