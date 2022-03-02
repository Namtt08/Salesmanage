package org.project.manage.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BaseResponse {
	private int result_code;
	private String result_description;
	private Object data;

	public BaseResponse(int result_code, String result_description) {
		this.result_code = result_code;
		this.result_description = result_description;
	}

	public BaseResponse(int result_code, String result_description, Object data) {
		this.result_code = result_code;
		this.result_description = result_description;
		this.data = data;
	}
}
