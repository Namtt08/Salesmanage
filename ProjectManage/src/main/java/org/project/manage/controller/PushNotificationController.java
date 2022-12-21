package org.project.manage.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.project.manage.dto.PnsRequestDto;
import org.project.manage.dto.PresenterRequestDto;
import org.project.manage.dto.PushNotificationRequest;
import org.project.manage.entities.SystemSetting;
import org.project.manage.entities.User;
import org.project.manage.entities.UserIntroducedEntity;
import org.project.manage.exception.AppException;
import org.project.manage.repository.UserIntroducedRepository;
import org.project.manage.repository.UserRepository;
import org.project.manage.request.UpdateUserInfo;
import org.project.manage.response.ApiResponse;
import org.project.manage.response.DocumentContractResponse;
import org.project.manage.response.DocumentInfoResponse;
import org.project.manage.response.MessageResponse;
import org.project.manage.response.MessageSuccessResponse;
import org.project.manage.response.NotificationDetailResponse;
import org.project.manage.response.PaymentHistoryDto;
import org.project.manage.response.PresenterResponse;
import org.project.manage.response.UpdateTokenResponse;
import org.project.manage.response.UserInfoResponse;
import org.project.manage.response.UserUpdateNotificationResponse;
import org.project.manage.services.FCMService;
import org.project.manage.services.SystemSettingService;
import org.project.manage.services.UserService;
import org.project.manage.util.AppConstants;
import org.project.manage.util.AppResultCode;
import org.project.manage.util.ErrorHandler;
import org.project.manage.util.MessageResult;
import org.project.manage.util.SuccessHandler;
import org.project.manage.util.SystemConfigUtil;
import org.project.manage.util.SystemSettingConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class PushNotificationController {

    @Autowired
    private FCMService fcmService;

    @PostMapping("/notification")
    public String sendSampleNotification(@RequestBody PushNotificationRequest pushNotificationRequest) {
        return fcmService.pushNotification(pushNotificationRequest);
    }
	
}
