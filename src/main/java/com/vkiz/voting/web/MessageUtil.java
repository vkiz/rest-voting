package com.vkiz.voting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {
    private final MessageSource messageSource;

    @Autowired
    public MessageUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getErrorMessage(String code, String[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public String getErrorMessage(String code) {
        return getErrorMessage(code, null);
    }
}
