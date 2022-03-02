package org.project.manage.util;

import javax.servlet.http.HttpServletRequest;

import org.project.manage.exception.AppException;
import org.project.manage.response.ApiResponse;
import org.project.manage.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandler {

	@Autowired
	private HttpServletRequest request;

	public ApiResponse handlerException(Exception ex, long start) {
		long took = System.currentTimeMillis() - start;
		if (ex instanceof AppException) {
			return new ApiResponse(took, new MessageResponse(AppResultCode.AS_ERROR, ex.getMessage()));
		}
		return new ApiResponse(AppResultCode.ERROR, ex.getMessage(), took);
	}

	public ApiResponse handlerException(long took) {

		return new ApiResponse(took);
	}
}
