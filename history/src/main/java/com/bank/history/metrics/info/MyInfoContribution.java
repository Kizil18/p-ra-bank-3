package com.bank.history.metrics.info;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * Информация о микросервисе history, для /actuator/info.
 */
@Component
public class MyInfoContribution implements InfoContributor {
    private final String time = new Date().toString();

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("project", Map.of("version", "0.0.1-SNAPSHOT", "name", "history",
                "time", time, "context-path", "api/history"));
    }
}
