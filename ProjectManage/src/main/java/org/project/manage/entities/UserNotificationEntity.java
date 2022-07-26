package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name = "user_notification")
@Data
public class UserNotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    
    private Long notificationTemplateId;
    
    private String notiType;
    
    private String title;
    
    private String content;
    
    private Date createdDate;
    
    private boolean seen;
    
    private Date deleteDate;
    

}
