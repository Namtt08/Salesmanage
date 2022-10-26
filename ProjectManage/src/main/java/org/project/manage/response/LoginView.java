package org.project.manage.response;

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
	private String avatar;
	private String dob;
	private String phoneNumber2;
	private String userType;

	public LoginView(String cuid, String phoneNumber, String token, String email,
			boolean isBlockUser, String nationalId, String gender, String fullName, String avatar, String dob, String phoneNumber2, String userType) {
		this.cuid = cuid;
		this.phoneNumber = phoneNumber;
		this.token = token;
		this.email = email;
		this.isBlockUser = isBlockUser;
		this.nationalId = nationalId;
		this.gender = gender;
		this.fullName = fullName;
		this.avatar = avatar;
		this.dob = dob;
		this.phoneNumber2 = phoneNumber2;
		this.userType = userType;
	}
	
}
