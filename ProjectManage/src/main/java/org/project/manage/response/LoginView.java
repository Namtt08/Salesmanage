package org.project.manage.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginView {

	private String cuid;
	private String phoneNumber;
	private String token;
	private String email;
	private boolean isBlockUser;
	private String nationalId;
	private String gender;
	private String fullName;

}
