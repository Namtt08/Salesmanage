package org.project.manage.response;

import java.util.List;

import org.project.manage.dto.ProductCategoryDto;
import org.project.manage.entities.UserNotificationEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class NotificationDetailResponse extends MessageSuccessResponse {
	
	List<UserNotificationEntity> listNoti;

}
