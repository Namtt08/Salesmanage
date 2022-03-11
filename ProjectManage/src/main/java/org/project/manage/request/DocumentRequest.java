package org.project.manage.request;

import lombok.Data;

@Data
public class DocumentRequest {

	private String fileContent;
	private String docType;
	private String fileName;

}
