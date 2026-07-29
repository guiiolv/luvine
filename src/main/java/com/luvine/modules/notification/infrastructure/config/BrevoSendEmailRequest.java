package com.luvine.modules.notification.infrastructure.config;

import java.util.List;
import java.util.Map;

public record BrevoSendEmailRequest(
        BrevoSender sender,
        List<BrevoRecipient> to,
        Long templateId,
        Map<String, Object> params
) {
}