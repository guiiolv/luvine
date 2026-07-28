package com.luvine.modules.notification.infrastructure.service;

import com.luvine.common.domain.exception.NotificationDeliveryException;
import com.luvine.common.domain.util.EmailMaskUtil;
import com.luvine.modules.notification.domain.service.EmailSenderService;
import com.luvine.modules.notification.domain.valueobject.EmailTemplate;
import com.luvine.modules.notification.infrastructure.client.BrevoEmailApi;
import com.luvine.modules.notification.infrastructure.config.BrevoProperties;
import com.luvine.modules.notification.infrastructure.config.BrevoRecipient;
import com.luvine.modules.notification.infrastructure.config.BrevoSendEmailRequest;
import com.luvine.modules.notification.infrastructure.config.BrevoSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class BrevoEmailSenderService implements EmailSenderService {

    private final BrevoEmailApi brevoEmailApi;
    private final BrevoProperties properties;

    public BrevoEmailSenderService(BrevoEmailApi brevoEmailApi, BrevoProperties properties) {
        this.brevoEmailApi = brevoEmailApi;
        this.properties = properties;
    }

    @Override
    public void send(EmailTemplate template, String recipientEmail, String recipientName, Map<String, Object> params) {
        Long templateId = properties.templates().get(template);

        BrevoSendEmailRequest request = new BrevoSendEmailRequest(
                new BrevoSender(properties.senderName(), properties.senderEmail()),
                List.of(new BrevoRecipient(recipientEmail, recipientName)),
                templateId,
                params);

        try {
            brevoEmailApi.sendTransactionalEmail(request);

            log.info(
                    "E-mail transacional enviado com sucesso. Template: {}, Destinatário: {}",
                    template,
                    EmailMaskUtil.mask(recipientEmail)
            );
        } catch (RestClientException ex) {
            log.error(
                    "Falha ao enviar e-mail transacional pelo provedor. Template: {}, Destinatário: {}",
                    template,
                    EmailMaskUtil.mask(recipientEmail),
                    ex
            );

            throw new NotificationDeliveryException("Não foi possível enviar o e-mail de verificação.", ex);
        }
    }
}