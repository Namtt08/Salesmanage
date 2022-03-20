package org.project.manage.response;

import java.util.List;

import org.project.manage.request.FileContentRequest;

import lombok.Data;

@Data
public class DocumentResponse {
	private String docType;
	private String fileName;
	private String docName;
	private Integer totalDoc;
	private String uploadType;
	private List<FilePathRespone> paths;

}
