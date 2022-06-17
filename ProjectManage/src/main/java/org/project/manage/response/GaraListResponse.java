package org.project.manage.response;

import lombok.Data;

@Data
public class GaraListResponse {
	private int codeStatus;
	private String messageStatus;

	public GaraListResponse(int code, String message) {
		this.codeStatus = code;
	    this.messageStatus = message;
	  }
}
