package com.bank.antifraud.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.EndpointWebExtension;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * расширение конечной точки info актуатора
 */
@Component
@EndpointWebExtension(endpoint = InfoEndpoint.class)
public class InfoWebEndpointExtension {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    @Value("${spring.application.name}")
    private String appName;
    @Value("${project.version}")
    private String projectVersion;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @ReadOperation
    public WebEndpointResponse<Map<String, Object>> info() {
        final Map<String, Object> info = new LinkedHashMap<>();
        info.put("application-name", appName);
        info.put("project-version", projectVersion);
        info.put("context-path", contextPath);
        info.put("started-at", dtf.format(now));
        return new WebEndpointResponse<>(info, HttpStatus.OK.value());
    }
}
