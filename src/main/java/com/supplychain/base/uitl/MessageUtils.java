package com.supplychain.base.uitl;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageUtils {

    private static MessageSource messageSource;

    // Spring اینو مقداردهی می‌کنه
    public MessageUtils(MessageSource messageSource) {
	MessageUtils.messageSource = messageSource;
    }

    public static String getMessage(String key, Object... args) {
	return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}
