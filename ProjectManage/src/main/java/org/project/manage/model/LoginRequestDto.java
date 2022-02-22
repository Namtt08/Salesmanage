package org.project.manage.model;

import lombok.Data;

@Data
public class LoginRequestDto {

	private String username;
	private String password;

	/**
	 * Create an empty LoginRequest object
	 */
	public LoginRequestDto() {
		super();
	}
	
	public LoginRequestDto(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
}
