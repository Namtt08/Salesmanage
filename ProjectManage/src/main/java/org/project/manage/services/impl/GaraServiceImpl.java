package org.project.manage.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.project.manage.entities.GaraInfoEntity;
import org.project.manage.repository.GaraRepository;
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
	public List<GaraInfoEntity> getAllGaraInfo() {
		try {
			List<GaraInfoEntity> listGara = new ArrayList<>();
			listGara = garaRepository.findAll();
			return listGara;
		} catch (Exception e) {
			log.error("#getAllProductCategory#ERROR#:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}


}
