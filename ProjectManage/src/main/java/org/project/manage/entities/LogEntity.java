package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "log")
@Data
public class LogEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
