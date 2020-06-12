package com.bothome.project_bot.service;

import lombok.Value;
import org.jvnet.hk2.annotations.Service;
import org.springframework.context.MessageSource;

import java.security.MessageDigest;
import java.util.Locale;

@Service
public class LocaleMessageService {
    private final Locale locale;
    private final MessageSource messageSource;

    public LocaleMessageService(String localeTag, MessageSource messageSource) {
        this.messageSource = messageSource;
        this.locale = Locale.forLanguageTag(localeTag);
    }

    public String getMessage(String message) {return messageSource.getMessage(message, null, locale); }

    public String getMessage(String message, Object... args) {return messageSource.getMessage(message, args, locale); }

}
