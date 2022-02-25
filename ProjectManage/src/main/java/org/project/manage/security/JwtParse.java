package org.project.manage.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;
import org.springframework.util.StringUtils;

public class JwtParse {

    public static String getJwtFromRequest(HttpServletRequest request) {
    	String headerAuth = request.getHeader("Authorization");
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}
		return null;
    }
}
