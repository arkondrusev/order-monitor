package com.example.ordermonitor.telegram;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {

    @Override
    public void consume(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println(update.getMessage().getText());
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("Initializing TelegramBot");
        try {
            String botToken = "7133584346:AAEafL7uILp5eIgaTsEagd-b_qvW6PJFvfg";
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, this);
        } catch (Throwable e) {
            System.out.println(e);
        }
    }

}
