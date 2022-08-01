package org.project.manage.services.impl;

import java.util.Date;

import org.project.manage.dto.UserNotificationDto;
import org.project.manage.entities.UserNotificationEntity;
import org.project.manage.repository.UserNotificationRepository;
import org.project.manage.services.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserNotificationServiceImpl implements UserNotificationService {

	@Autowired
	private UserNotificationRepository userNotificationRepository;

	@Override
	public void save(UserNotificationDto userNotificationDto) {
		UserNotificationEntity entity=   new UserNotificationEntity();
		entity.setUserId(userNotificationDto.getUserId());
		entity.setNotificationTemplateId(userNotificationDto.getNotificationTemplateId());
		entity.setContent(userNotificationDto.getContent());
		entity.setCreatedDate(new Date());
		entity.setNotiType(userNotificationDto.getNotiType());
		entity.setTitle(userNotificationDto.getTitle());
		entity.setSeen(true);
		userNotificationRepository.save(entity);
		
	}
	

}
