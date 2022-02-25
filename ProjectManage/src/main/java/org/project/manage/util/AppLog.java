package org.project.manage.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class AppLog {

    private Request request;

    private Response response;

    public void writeWithLogger(String marker) {
        log.info("{} -- Request: {} -- Response: {}", marker, this.request.toString(), this.response.toString());
    }

    public void writeWithLoggerRequest(String marker) {
        log.info("{} -- Request: {}", marker, this.request.toString());
    }

    public void writeWithLoggerResponse(String marker) {
        log.info("{} -- Response: {}", marker, this.response.toString());
    }

    @Data
    @AllArgsConstructor
    public static class Request {

        private String requestMethod;

        private String requestUrl;

        private String header;

        private String requestBody;
        
        private int totalConnections;
        
        private int activeConnections;
        
        private int idleConnections;
        
        private int idleThreadsAwaitingConnection;
    }

    @Data
    @AllArgsConstructor
    public static class Response {

        private long took;

        private String status;

        private Object responseBody;

        private int totalConnections;

        private int activeConnections;

        private int idleConnections;

        private int idleThreadsAwaitingConnection;
    }
}
