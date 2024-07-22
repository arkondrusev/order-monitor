package com.example.ordermonitor.telegram.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramConfig {

    private final String botToken;

    public TelegramConfig(@Value("${telegram.bot-token}") String botToken) {
        this.botToken = botToken;
    }

    public String getBotToken() {
        return botToken;
    }

}
