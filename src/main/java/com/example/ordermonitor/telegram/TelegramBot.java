package com.example.ordermonitor.telegram;

import com.example.ordermonitor.telegram.config.TelegramConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private TelegramConfig config;
    private TelegramClient telegramClient;

    public TelegramBot(TelegramConfig telegramConfig) {
        this.config = telegramConfig;
        this.telegramClient = new OkHttpTelegramClient(telegramConfig.getBotToken());
    }

    @Override
    public void consume(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println(update.getMessage().getText());

            try {
                sendMessage(update.getMessage().getChatId().toString(), "Received text: " + update.getMessage().getText());
            } catch (TelegramApiException e) {
                System.out.println(e);
            }
        }
    }

    public void sendMessage(String chatId, String text) throws TelegramApiException {
        telegramClient.execute(new SendMessage(chatId, text));
    }

    @PostConstruct
    public void init() {
        System.out.println("Initializing TelegramBot");
        try {
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(config.getBotToken(), this);
        } catch (Throwable e) {
            System.out.println(e);
        }
    }

}
