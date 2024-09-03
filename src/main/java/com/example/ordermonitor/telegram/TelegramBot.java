package com.example.ordermonitor.telegram;

import com.example.ordermonitor.model.ApiAccount;
import com.example.ordermonitor.service.ApiAccountService;
import com.example.ordermonitor.telegram.config.TelegramConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Optional;

@Component
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramConfig config;
    private final TelegramClient telegramClient;
    private final ApiAccountService accountService;
    private final List<ApiAccount> apiAccountList;
    private final ApiAccountService apiAccountService;


    public TelegramBot(TelegramConfig telegramConfig, ApiAccountService accountService, ApiAccountService apiAccountService) {
        this.config = telegramConfig;
        this.telegramClient = new OkHttpTelegramClient(telegramConfig.getBotToken());
        this.accountService = accountService;

        apiAccountList = accountService.getApiAccountList();
        this.apiAccountService = apiAccountService;
    }

    @Override
    public void consume(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println(update.getMessage().getText());
            Message message = update.getMessage();
            String messageText = message.getText();
            if ("/start".equals(messageText) || "/stop".equals(messageText)) {
                Optional<ApiAccount> apiAccount = findApiAccount(message.getFrom().getUserName());
                if (apiAccount.isPresent()) {
                    String messageForSending = null;
                    if ("/start".equals(messageText)) {
                        apiAccount.get().setTelegramChatId(message.getChatId().toString());
                        messageForSending = "Notification enabled";
                    } else if ("/stop".equals(messageText)) {
                        apiAccount.get().setTelegramChatId(null);
                        messageForSending = "Notification disabled";
                    }
                    apiAccountService.saveApiAccount(apiAccount.get());
                    sendMessage(message.getChatId().toString(), messageForSending);
                }
            }
        }
    }

    private Optional<ApiAccount> findApiAccount(String telegramUsername) {
        return apiAccountList.stream().filter(each -> each.getTelegramUsername().equals(telegramUsername)).findFirst();
    }

    public void sendMessage(String chatId, String text) {
        try {
            telegramClient.execute(new SendMessage(chatId, text));
        } catch (TelegramApiException e) {
            System.out.println(e);
        }
    }

    @PostConstruct
    private void init() {
        System.out.println("Initializing TelegramBot");
        try {
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(config.getBotToken(), this);
        } catch (Throwable e) {
            System.out.println(e);
        }
    }

}
