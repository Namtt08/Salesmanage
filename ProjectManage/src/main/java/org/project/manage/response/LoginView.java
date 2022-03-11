package org.project.manage.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginView extends MessageSuccessResponse {

	private String cuid;
	private String phoneNumber;
	private String token;
	private String email;
	private boolean isBlockUser;
	private String nationalId;
	private String gender;
	private String fullName;

	public LoginView(String cuid, String phoneNumber, String token, String email,
			boolean isBlockUser, String nationalId, String gender, String fullName) {
		this.cuid = cuid;
		this.phoneNumber = phoneNumber;
		this.token = token;
		this.email = email;
		this.isBlockUser = isBlockUser;
		this.nationalId = nationalId;
		this.gender = gender;
		this.fullName = fullName;
	}
	
}
