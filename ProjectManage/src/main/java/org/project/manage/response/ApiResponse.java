package org.project.manage.response;

import java.io.Serializable;

import org.project.manage.util.AppResultCode;
import org.project.manage.util.MessageResult;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse extends BaseResponse implements Serializable {

	private static final long serialVersionUID = -2080447665304438927L;

	private String time;

	private long took;

	private Object data;

	public ApiResponse(long took, Object data) {
		this.took = took;
		this.data = data;
		this.setResult_code(AppResultCode.SUCCESS);
		this.setResult_description(MessageResult.SUCCESS);
	}

	public ApiResponse(long took) {
		this.setResult_code(AppResultCode.ERROR);
		this.setResult_description(MessageResult.ERROR_COMMON);
	}

}
