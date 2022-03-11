package org.project.manage.util;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.JoinPoint;
import org.project.manage.security.JwtParse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;


public class RequestUtil {

    private static final int LENGTH = 8;

    public static String getDeviceFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AppConstants.DEVICE_ID)).orElse(Strings.EMPTY);
    }


    public static String getFullRequestUrl(HttpServletRequest request) {
        String requestURL = String.valueOf(request.getRequestURL());
        String queryString = request.getQueryString();
        if (Strings.isNotBlank(queryString)) {
            return String.join("?", requestURL, queryString);
        }
        return requestURL;
    }

    public static String getBody(HttpServletRequest request, JoinPoint joinPoint, ObjectMapper jsonMapper) {
        String body = "";
        String reqMethod = request.getMethod();
        if ("POST".equals(reqMethod) || "PUT".equals(reqMethod) || "DELETE".equals(reqMethod)) {
            Object obj = getBodyObject(joinPoint);
            if (obj == null) return body;
            try {
                body += jsonMapper.writeValueAsString(obj);
            } catch (JsonProcessingException ignore) {
            }
        }
        return body;
    }

    private static Object getBodyObject(JoinPoint joinPoint) {
        Object obj = Stream.of(joinPoint.getArgs())
                .filter(Objects::nonNull)
                .filter(o -> {
                    Class<?> aClass = o.getClass();
                    String name = aClass.getName();
                    if (o instanceof List) {
                        List<?> list = (List) o;
                        if (!CollectionUtils.isEmpty(list)) {
                            name = list.get(0).getClass().getName();
                        }
                    }
                    return name.contains("org.project.manage") || name.contains("util");
                })
                .findAny().orElse(null);
        return obj;
    }

    public static String getInfoMandatoryHeader(HttpServletRequest request) {
        Map<String, String> map = Maps.newHashMap();
        String jwt = JwtParse.getJwtFromRequest(request);
        if (Strings.isNotBlank(jwt) && jwt.length() > RequestUtil.LENGTH) {
            String subStringLast = StringUtils.substring(jwt, jwt.length() - RequestUtil.LENGTH);
            jwt = StringUtils.repeat("*", 4) + StringUtils.repeat(".", 3) + subStringLast;
        }
        String deviceId = request.getHeader(AppConstants.DEVICE_ID);
        map.put(AppConstants.TOKEN, jwt);
        map.put(AppConstants.DEVICE_ID, deviceId);
        return map.toString();
    }

    public static HttpEntity<Object> entity(Object body) {
        return new HttpEntity<>(body, null);
    }

    public static HttpEntity<Object> entity(Object body, String partnerCode) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Accept","application/pgp-encrypted");
        headers.set("Content-Type", "application/pgp-encrypted");
        headers.set("partner-code",partnerCode);
        return new HttpEntity<>(body, headers);
    }
    
    public static HttpEntity<Object> entity(Object body, String partnerCode, String checksum) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Content-Checksum",checksum);
        return new HttpEntity<>(body, headers);
    }
    
    public static HttpEntity<Object> entity(Object body, String partnerCode, String checksum,String clientId) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-Airpay-ClientId",clientId);
        headers.set("X-Airpay-Req-H",checksum);
        return new HttpEntity<>(body, headers);
    }
    
    public static HttpEntity<Object> entityVnPay() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        return new HttpEntity<>(headers);
    }
    
}
