package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "document")
@Data
public class Document {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long docInfoId;
	private Long userId;
	private String docPath;
	private String docType;
	private String createdBy;
	private Date createdDate;
	private Date modifiedDate;
	private Date deletedDate;
	private Long priority;
	public Document() {
	}
}
