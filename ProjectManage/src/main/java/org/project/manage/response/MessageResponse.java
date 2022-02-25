package org.project.manage.response;

import lombok.Data;

@Data
public class MessageResponse {
	private int code;
	private String message;

	public MessageResponse(int code, String message) {
		this.code = code;
	    this.message = message;
	  }
}
