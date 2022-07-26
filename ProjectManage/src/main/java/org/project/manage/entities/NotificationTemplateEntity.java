package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "notification_template")

public class NotificationTemplateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String notiType;
	private String title;
	private String body;
	private int status;
	private Date createdDate;
	private boolean seen;
	
	public NotificationTemplateEntity() {
	}
}
