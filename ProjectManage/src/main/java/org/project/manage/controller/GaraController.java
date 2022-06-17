package org.project.manage.controller;

import java.util.List;

import org.project.manage.entities.GaraInfoEntity;
import org.project.manage.response.ApiResponse;
import org.project.manage.services.GaraService;
import org.project.manage.util.ErrorHandler;
import org.project.manage.util.SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/gara")
@Slf4j
public class GaraController {

	@Autowired
	private SuccessHandler successHandler;

	@Autowired
	private ErrorHandler errorHandler;

	@Autowired
	private GaraService garaService;

	@PostMapping("/list")
	public ApiResponse getListGara() {
		long start = System.currentTimeMillis();
		try {
			List<GaraInfoEntity> response = this.garaService.getAllGaraInfo(); 
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#getListGara#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}

}

