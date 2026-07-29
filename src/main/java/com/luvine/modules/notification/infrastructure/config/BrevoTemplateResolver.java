package com.luvine.modules.notification.infrastructure.config;

import com.luvine.common.domain.exception.EmailTemplateNotConfiguredException;
import com.luvine.common.domain.exception.NotificationDeliveryException;
import com.luvine.modules.notification.domain.valueobject.EmailTemplate;
import org.springframework.stereotype.Component;

@Component
public class BrevoTemplateResolver {

    private final BrevoProperties properties;

    public BrevoTemplateResolver(BrevoProperties properties) {
        this.properties = properties;
    }

    public Long resolve(EmailTemplate template) {
        Long templateId = properties.templates().get(template);

        if (templateId == null) {
            throw new EmailTemplateNotConfiguredException("Nenhum template Brevo configurado para o ID informado.");
        }

        return templateId;
    }
}