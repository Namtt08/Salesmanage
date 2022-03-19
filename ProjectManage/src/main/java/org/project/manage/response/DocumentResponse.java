package org.project.manage.response;

import java.util.List;

import org.project.manage.request.FileContentRequest;

import lombok.Data;

@Data
public class DocumentResponse {

	private List<FilePathRespone> files;
	private String docType;
	private String fileName;
	private String docName;

}
