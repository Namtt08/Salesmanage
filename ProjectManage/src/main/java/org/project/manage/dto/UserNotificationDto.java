package org.project.manage.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
public class UserNotificationDto {

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
