package org.project.manage.util;

import javax.servlet.http.HttpServletRequest;

import org.project.manage.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandler {

	@Autowired
	private HttpServletRequest request;

	public ApiResponse handlerException(long took) {

		 return new ApiResponse(took);
	}
}
