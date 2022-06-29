package org.project.manage.dto;

import java.util.Date;

import lombok.Data;

@Data
public class LogDto {
	private Long id;
	
	private Long userId;

	private String actionType;
	
	private Date startDate;
	
	private Date endDate;
	
	private String url;
	
	private String request;
	
	private String response;
	
	private String message;
	
	private Date createdDate;

	private String createdBy;
}
