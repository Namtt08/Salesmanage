package org.project.manage.response;

import lombok.Data;

@Data
public class UserInfoResponse extends MessageSuccessResponse{
	private String cuid;
	private String nationalId;
	private String dob;
	private String fullName;
	private String phoneNumber;
	private String email;
	private String gender;
	private String phoneNumber2;
	private String avatar;
	

	public UserInfoResponse(String cuid, String nationalId, String dob, String fullName,
			String phoneNumber, String email, String gender, String phoneNumber2, String avatar) {
		this.cuid = cuid;
		this.nationalId = nationalId;
		this.dob = dob;
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.gender = gender;
		this.phoneNumber2 = phoneNumber2;
		this.avatar = avatar;
	}
}
