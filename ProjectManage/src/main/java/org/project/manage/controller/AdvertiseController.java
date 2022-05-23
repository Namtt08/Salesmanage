package org.project.manage.controller;

import java.nio.file.Path;
import java.util.List;

import org.project.manage.entities.Document;
import org.project.manage.response.ApiResponse;
import org.project.manage.services.AdvertisementService;
import org.project.manage.util.ErrorHandler;
import org.project.manage.util.SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/advertisement")
@Slf4j
@EnableAsync
public class AdvertiseController {

	@Autowired
	private SuccessHandler successHandler;

	@Autowired
	private ErrorHandler errorHandler;

	protected Path fileStorageLocation;
	
	@Autowired
	private AdvertisementService advertisementService;

	public static final int CORE_POOL_SIZE = 0;

	public static final int MAXIMUM_POOL_SIZE = 10;

	@GetMapping("/list")
	public ApiResponse getCart() {
		long start = System.currentTimeMillis();
		try {
			//User user = getUserFromAuthentication();
			List <Document> response = advertisementService.getAdvertisementList();
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#deleteProductCart#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}

}
