package org.project.manage.config;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.UUID;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.project.manage.request.LoginRequest;
import org.project.manage.util.AppLog;
import org.project.manage.util.RequestUtil;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.zaxxer.hikari.HikariPoolMXBean;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Configuration
@Slf4j
public class AopLogging {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    @Qualifier("jsonMapper")
    private ObjectMapper objectMapper;
    
    @Value("${spring.datasource.hikari.poolName}")
    private String poolStringName;

    MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

    @Around("within(org.project.manage.controller..*)")
    public Object log(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ObjectName poolName = null;
        poolName = new ObjectName(String.format("com.zaxxer.hikari:type=Pool (%s)",poolStringName));
        HikariPoolMXBean poolProxy = JMX.newMXBeanProxy(mBeanServer, poolName, HikariPoolMXBean.class);
        MDC.put("traceID", UUID.randomUUID().toString());
        String method = this.request.getMethod();
        String fullRequestUrl = RequestUtil.getFullRequestUrl(this.request);
        boolean isSaveLog = true;
        isSaveLog = !fullRequestUrl.contains("api/log-connection-pool/status");

        String header = RequestUtil.getInfoMandatoryHeader(this.request);
        String body = RequestUtil.getBody(this.request, proceedingJoinPoint, this.objectMapper);
		boolean password = body.contains("password");
		if (password) {
			LoginRequest loginRequest = this.objectMapper.readValue(body, LoginRequest.class);
			loginRequest.setPassword("");
			body = this.objectMapper.writeValueAsString(loginRequest);
		} else {
			body = mySubString(body, 0, 500);
		}

        AppLog.Request request = new AppLog.Request(method, fullRequestUrl, header, body, poolProxy.getTotalConnections(),
                poolProxy.getActiveConnections(), poolProxy.getIdleConnections(), poolProxy.getThreadsAwaitingConnection());
        MDC.put("header", header);
        if (isSaveLog) {
            new AppLog(request, null).writeWithLoggerRequest("#REQUEST#");
        }
        Object result;
        long start = System.currentTimeMillis();
        result = proceedingJoinPoint.proceed();
        long took = System.currentTimeMillis() - start;

        String resBody = "";

        if (result instanceof ResponseEntity) {
            @SuppressWarnings("rawtypes")
            Object resEntityBody = ((ResponseEntity) result).getBody();
			if (resEntityBody != null) {
				resBody = resEntityBody.toString();
			}
		} else {
			try {
				resBody = this.objectMapper.writeValueAsString(result);
				resBody = mySubString(resBody, 0, 500);

			} catch (Exception e) {
				log.error("log: "+ e.getMessage());
			}
		}
        AppLog.Response response = new AppLog.Response(took, null, resBody, poolProxy.getTotalConnections(),
                poolProxy.getActiveConnections(), poolProxy.getIdleConnections(), poolProxy.getThreadsAwaitingConnection());
        if (isSaveLog) {
            new AppLog(null, response).writeWithLoggerResponse("#RESPONSE#");
        }
        // new AppLog(request, response).writeWithLogger("USER_REQUEST");

        return result;
    }

	private String mySubString(String myString, int start, int length) {
		return myString.substring(start, Math.min(start + length, myString.length()));
	}
}
