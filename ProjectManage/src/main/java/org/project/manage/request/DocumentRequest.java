package org.project.manage.request;

import java.util.List;

import lombok.Data;

@Data
public class DocumentRequest {

	private List<FileContentRequest> files;
	private String docType;
	private String fileName;
	private String docName;

}
