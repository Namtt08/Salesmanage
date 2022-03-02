package org.project.manage.response;

import lombok.Data;

@Data
public class MessageResponse {
	private int codeStatus;
	private String messageStatus;

	public MessageResponse(int code, String message) {
		this.codeStatus = code;
	    this.messageStatus = message;
	  }
}
