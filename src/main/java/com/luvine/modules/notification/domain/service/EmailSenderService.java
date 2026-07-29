package com.luvine.modules.notification.domain.service;

import com.luvine.modules.notification.domain.valueobject.EmailTemplate;

import java.util.Map;

public interface EmailSenderService {

    void send(EmailTemplate template, String recipientEmail, String recipientName, Map<String, Object> params);
}