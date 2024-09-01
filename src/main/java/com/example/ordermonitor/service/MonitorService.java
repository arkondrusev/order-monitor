package com.example.ordermonitor.service;

import com.example.ordermonitor.dto.SEOrderWrapper;
import com.example.ordermonitor.mapper.SEOrderWrapper2StockExchangeOrderMapper;
import com.example.ordermonitor.model.StockExchange;
import com.example.ordermonitor.model.StockExchangeApiAccount;
import com.example.ordermonitor.model.StockExchangeOrder;
import com.example.ordermonitor.stockexch.ExchConfig;
import com.example.ordermonitor.stockexch.client.OkxClient;
import com.example.ordermonitor.telegram.TelegramBot;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Service
public class MonitorService {

    private final StockExchangeService stockExchangeService;
    private final StockExchangeApiAccountService stockExchangeApiAccountService;
    private final StockExchangeOrderService stockExchangeOrderService;
    private final TelegramBot telegramBot;
    private final Environment env;

    private final List<StockExchange> stockExchangeList = new ArrayList<>();
    private final Map<StockExchange, List<StockExchangeApiAccount>> stockExchangeApiAccountList = new HashMap<>();
    private final Map<StockExchangeApiAccount, List<StockExchangeOrder>> stockExchangeDBOrderList = new HashMap<>();

    public MonitorService(StockExchangeService stockExchangeService,
                          StockExchangeApiAccountService stockExchangeApiAccountService,
                          StockExchangeOrderService stockExchangeOrderService,
                          TelegramBot telegramBot,
                          Environment env) {
        this.stockExchangeService = stockExchangeService;
        this.stockExchangeApiAccountService = stockExchangeApiAccountService;
        this.stockExchangeOrderService = stockExchangeOrderService;
        this.telegramBot = telegramBot;
        this.env = env;

        initData();
    }

    private void initData() {
        stockExchangeService.getStockExchangeList().forEach(e -> {
            stockExchangeList.add(e);
            stockExchangeList.forEach(this::initStockExchangeData);
        });
    }

    private void initStockExchangeData(StockExchange se) {
        List<StockExchangeApiAccount> seApiAccountList = stockExchangeApiAccountService.getStockExchangeApiAccount(se);
        stockExchangeApiAccountList.put(se, seApiAccountList);
        seApiAccountList.forEach(acc -> {
            // load api config
            // rework later for flexible initialization
            if (se.getId().equals(1L)) {
                String propSubstr = se.getName().toLowerCase() + ".api." + acc.getName().toLowerCase();
                String apiKey = env.getProperty(propSubstr + ".key");
                String secretKey = env.getProperty(propSubstr + ".secretkey");
                String passphrase = env.getProperty(propSubstr + ".passphrase");
                acc.setExchClient(new OkxClient(new ExchConfig(apiKey, secretKey, passphrase)));
            }
            stockExchangeDBOrderList.put(acc, stockExchangeOrderService.getLiveStockExchangeOrderList(acc));
        });
    }

    @Scheduled(fixedDelay = 10000)
    public void scheduleCheckOrders() {
        checkExchangesOrders();
    }

    private void checkExchangesOrders() {
        stockExchangeList.forEach(se -> stockExchangeApiAccountList.get(se).forEach(acc -> {
            List<SEOrderWrapper> exchOrderList = acc.getExchClient().requestOrderList();
            List<StockExchangeOrder> newExchOrderList = new ArrayList<>();
            List<StockExchangeOrder> finishedExchOrderList = new ArrayList<>();
            findNewAndFinishedOrders(acc, exchOrderList, newExchOrderList, finishedExchOrderList);

            processNewExchOrders(acc, newExchOrderList);
            processFinishedExchOrders(acc, finishedExchOrderList);
        }));
    }

    private void findNewAndFinishedOrders(final StockExchangeApiAccount apiAccount,
                                        final List<SEOrderWrapper> exchOrderList,
                                        final List<StockExchangeOrder> newExchOrderList,
                                        final List<StockExchangeOrder> finishedExchOrderList) {
        final List<StockExchangeOrder> pFinishedExchOrderList = new ArrayList<>(stockExchangeDBOrderList.get(apiAccount));
        exchOrderList.forEach(exchOrder -> {
            // compare by exchange order number
            Optional<StockExchangeOrder> dbOrderOpt = pFinishedExchOrderList.stream()
                    .filter(dbOrder -> dbOrder.getSeOrderId().equals(exchOrder.getOrderId())).findFirst();
            if (dbOrderOpt.isPresent()) {
                pFinishedExchOrderList.remove(dbOrderOpt.get());
            } else {
                StockExchangeOrder order = SEOrderWrapper2StockExchangeOrderMapper
                        .INSTANCE.SEOrderWrapper2StockExchangeOrder(exchOrder, apiAccount);
                order.setExecuteTimestamp(null);
                newExchOrderList.add(order);
            }
        });

        finishedExchOrderList.addAll(pFinishedExchOrderList);
    }

    private void processNewExchOrders(final StockExchangeApiAccount apiAccount,
                                      final List<StockExchangeOrder> newExchOrderList) {
        // если на бирже есть а в БД нет, то сохранить в БД и послать уведомление в ТГ
        newExchOrderList.forEach(e -> {
            stockExchangeDBOrderList.get(apiAccount).add(stockExchangeOrderService.save(e));
            try {
                telegramBot.sendMessage("376653873", "OKX new order");
            } catch (TelegramApiException ex) {
                System.out.println(ex);
            }
        });
    }

    private void processFinishedExchOrders(final StockExchangeApiAccount apiAccount,
                                           final List<StockExchangeOrder> finishedExchOrderList) {
        // если в БД есть а на бирже нет, то запросить по дельте статус и обновить статусы в БД,
        // после чего послать уведомление в ТГ
        finishedExchOrderList.forEach(e -> {
            requestAndUpdateOrder(e, apiAccount);
            stockExchangeDBOrderList.get(apiAccount).remove(e);
            try {
                telegramBot.sendMessage("376653873", "OKX finished order");
            } catch (Exception ex) {
                System.out.println(ex);
            }
        });
    }

    private void requestAndUpdateOrder(StockExchangeOrder order, StockExchangeApiAccount apiAccount) {
        SEOrderWrapper wrapper = apiAccount.getExchClient()// сделать метод запроса в апи аккауне
                .requestOrderDetails(order.getInstrument(), order.getSeOrderId());
        StockExchangeOrder exchOrder = SEOrderWrapper2StockExchangeOrderMapper
                .INSTANCE.SEOrderWrapper2StockExchangeOrder(wrapper, order.getStockExchangeApiAccount());
        order.setExecuteTimestamp(exchOrder.getExecuteTimestamp());
        order.setState(exchOrder.getState());
        stockExchangeOrderService.save(order);
    }

}
