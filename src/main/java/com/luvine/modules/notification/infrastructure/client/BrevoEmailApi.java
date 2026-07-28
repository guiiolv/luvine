package com.luvine.modules.notification.infrastructure.client;

import com.luvine.modules.notification.infrastructure.config.BrevoSendEmailRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface BrevoEmailApi {

    @PostExchange("/smtp/email")
    void sendTransactionalEmail(@RequestBody BrevoSendEmailRequest request);
}