package org.project.manage.util;

import org.project.manage.entities.User;
import org.project.manage.exception.AppException;
import org.project.manage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

public class CommonUtil {

	@Autowired
	private static UserService userService;
	
	public static User getUserFromAuthentication() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		return userService.findByUsername(name)
				.orElseThrow(() -> new AppException(MessageResult.GRD004_NOT_FOUND));
		
	}
}
