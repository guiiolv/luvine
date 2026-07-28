package com.luvine.modules.notification.infrastructure.config;

import com.luvine.modules.notification.domain.valueobject.EmailTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "app.brevo")
public record BrevoProperties(
        String apiKey,
        String recipientEmail,
        String recipientName,
        Map<EmailTemplate, Long> templates
) {
}