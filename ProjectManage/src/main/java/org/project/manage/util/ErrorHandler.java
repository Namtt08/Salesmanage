package org.project.manage.util;

import org.project.manage.exception.AppException;
import org.project.manage.response.ApiResponse;
import org.project.manage.response.MessageResponse;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandler {

//	@Autowired
//	private HttpServletRequest request;

	public ApiResponse handlerException(Exception ex, long start) {
		long took = System.currentTimeMillis() - start;
		if (ex instanceof AppException) {
			return new ApiResponse(took, new MessageResponse(AppResultCode.AS_ERROR, ex.getMessage()));
		}
		return new ApiResponse(took, new MessageResponse(AppResultCode.ERROR, ex.getMessage()));
	}

	public ApiResponse handlerException(long took) {

		return new ApiResponse(took);
	}
}
