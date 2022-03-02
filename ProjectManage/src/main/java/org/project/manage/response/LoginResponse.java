package org.project.manage.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends BaseResponse {
	private LoginView data;

	public LoginResponse(int result_code, String result_description, LoginView data) {
		super(result_code, result_description);
		this.data = data;
	}
}
