package org.project.manage.util;


import org.project.manage.response.ApiResponse;
import org.springframework.stereotype.Component;

@Component
public class SuccessHandler {

    public ApiResponse handlerSuccess(Object data, long start) {
        long took = System.currentTimeMillis() - start;
        return new ApiResponse(took, data);
    }

}
