package org.project.manage.controller;

import org.project.manage.dto.GaraInfoDto;
import org.project.manage.entities.User;
import org.project.manage.exception.AppException;
import org.project.manage.response.ApiResponse;
import org.project.manage.response.GaraDetailResponse;
import org.project.manage.response.GaraListResponse;
import org.project.manage.response.ProductDetailResponse;
import org.project.manage.services.GaraService;
import org.project.manage.services.UserService;
import org.project.manage.util.ErrorHandler;
import org.project.manage.util.MessageResult;
import org.project.manage.util.SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

	@Autowired
	private UserService userService;

	@PostMapping("/list")
	public ApiResponse getListGara() {
		long start = System.currentTimeMillis();
		try {
			GaraListResponse response = this.garaService.getAllGaraInfo(); 
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#getListGara#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}

	@GetMapping("/detail")
	public ApiResponse getGaraDetail(@RequestParam(value = "id", required = true) Long id) {
		long start = System.currentTimeMillis();
		try {
			GaraDetailResponse response = this.garaService.getGaraDetail(id);
			return this.successHandler.handlerSuccess(response, start);
		} catch (Exception e) {
			log.error("#getProductDetail#ERROR#:" + e.getMessage());
			e.printStackTrace();
			return this.errorHandler.handlerException(e, start);
		}
	}

}

