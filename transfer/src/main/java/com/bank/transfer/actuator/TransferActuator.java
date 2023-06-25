package com.bank.transfer.actuator;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * Информация о микросервисе transfer, для /actuator/info.
 */
@Component
public class TransferActuator implements InfoContributor {
    private final String time = new Date().toString();

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("transfer-info", Map.of("version", "0.0.1-SNAPSHOT", "name", "transfer",
                "time", time, "context-path", "api/transfer"));
    }
}
