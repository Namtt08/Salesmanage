package org.project.manage.services;

import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.project.manage.dto.PnsRequestDto;
import org.project.manage.dto.PushNotificationRequest;
import org.project.manage.dto.UserNotificationDto;
import org.project.manage.repository.ProductRepository;
import org.project.manage.repository.UserNotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
@Transactional
public class FCMService {
	 
    private Logger logger = LoggerFactory.getLogger(FCMService.class);
    
    @Autowired
    private UserNotificationService UserNotificationService;
    
    public String pushNotification(PushNotificationRequest pushNotificationRequest) {
    	
    	
    	PnsRequestDto pnsRequest =  new PnsRequestDto();
    	pnsRequest.setFcmToken(pushNotificationRequest.getToken());
    	pnsRequest.setContent(pushNotificationRequest.getBody());
    	Notification notification = new Notification (pushNotificationRequest.getTitle(),pushNotificationRequest.getBody());
    	
    	Message message = Message.builder()
                .putData("content", pnsRequest.getContent())
                .setToken(pnsRequest.getFcmToken())
                .setNotification(notification)
                .build();

        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
            
            UserNotificationService.save(UserNotificationDto(pushNotificationRequest));
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return response;
    }

	private UserNotificationDto UserNotificationDto(PushNotificationRequest pushNotificationRequest) {
		UserNotificationDto userNotificationDto=  new UserNotificationDto(); 
		userNotificationDto.setUserId(pushNotificationRequest.getUserId());
		userNotificationDto.setNotificationTemplateId(pushNotificationRequest.getNotificationTemplateId());
		userNotificationDto.setContent(pushNotificationRequest.getContent());
		userNotificationDto.setCreatedDate(new Date());
		userNotificationDto.setNotiType(pushNotificationRequest.getType());
		userNotificationDto.setTitle(userNotificationDto.getTitle());
		
		return userNotificationDto;
	}

}
