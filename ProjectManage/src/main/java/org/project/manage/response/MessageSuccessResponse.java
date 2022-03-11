package org.project.manage.response;

import org.project.manage.util.AppResultCode;
import org.project.manage.util.MessageResult;

import lombok.Data;

@Data
public class MessageSuccessResponse {
	private int codeStatus;
	private String messageStatus;

	public MessageSuccessResponse() {
		this.codeStatus = AppResultCode.AS_SUCCESS;
		this.messageStatus = MessageResult.SUCCESS;
	}
}
