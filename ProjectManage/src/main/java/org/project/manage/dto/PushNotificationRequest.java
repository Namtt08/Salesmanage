package org.project.manage.dto;

import lombok.Data;

@Data
public class PushNotificationRequest {
	
    private String title;
    private String message;
    private String topic;
    private String token;
    
    private Long userId;

    private String body;
    private String image;
    private String content;
    private Long notificationTemplateId;
    
    private String type;
    
}
