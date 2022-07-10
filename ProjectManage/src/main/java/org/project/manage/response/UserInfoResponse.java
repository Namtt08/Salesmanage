package org.project.manage.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
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
	private int level;
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private Long totalAmount;
	private Long point;
	

	public UserInfoResponse(String cuid, String nationalId, String dob, String fullName,
			String phoneNumber, String email, String gender, String phoneNumber2, String avatar, int level, Long totalAmount) {
		this.cuid = cuid;
		this.nationalId = nationalId;
		this.dob = dob;
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.gender = gender;
		this.phoneNumber2 = phoneNumber2;
		this.avatar = avatar;
		this.level = level;
		this.totalAmount = totalAmount;
		
	}
}
