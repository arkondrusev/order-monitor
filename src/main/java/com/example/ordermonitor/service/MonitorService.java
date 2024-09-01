package com.example.ordermonitor.service;

import com.example.ordermonitor.dto.OrderWrapper;
import com.example.ordermonitor.mapper.SEOrderWrapper2StockExchangeOrderMapper;
import com.example.ordermonitor.model.StockExchange;
import com.example.ordermonitor.model.ApiAccount;
import com.example.ordermonitor.model.Order;
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
    private final OrderService stockExchangeOrderService;
    private final TelegramBot telegramBot;
    private final Environment env;

    private final List<StockExchange> stockExchangeList = new ArrayList<>();
    private final Map<StockExchange, List<ApiAccount>> stockExchangeApiAccountList = new HashMap<>();
    private final Map<ApiAccount, List<Order>> stockExchangeDBOrderList = new HashMap<>();

    public MonitorService(StockExchangeService stockExchangeService,
                          StockExchangeApiAccountService stockExchangeApiAccountService,
                          OrderService stockExchangeOrderService,
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
        List<ApiAccount> seApiAccountList = stockExchangeApiAccountService.getStockExchangeApiAccount(se);
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
            stockExchangeDBOrderList.put(acc, stockExchangeOrderService.getLiveOrderList(acc));
        });
    }

    @Scheduled(fixedDelay = 10000)
    public void scheduleCheckOrders() {
        checkExchangesOrders();
    }

    private void checkExchangesOrders() {
        stockExchangeList.forEach(se -> stockExchangeApiAccountList.get(se).forEach(acc -> {
            List<OrderWrapper> exchOrderList = acc.getExchClient().requestOrderList();
            List<Order> newExchOrderList = new ArrayList<>();
            List<Order> finishedExchOrderList = new ArrayList<>();
            findNewAndFinishedOrders(acc, exchOrderList, newExchOrderList, finishedExchOrderList);

            processNewExchOrders(acc, newExchOrderList);
            processFinishedExchOrders(acc, finishedExchOrderList);
        }));
    }

    private void findNewAndFinishedOrders(final ApiAccount apiAccount,
                                        final List<OrderWrapper> exchOrderList,
                                        final List<Order> newExchOrderList,
                                        final List<Order> finishedExchOrderList) {
        final List<Order> pFinishedExchOrderList = new ArrayList<>(stockExchangeDBOrderList.get(apiAccount));
        exchOrderList.forEach(exchOrder -> {
            // compare by exchange order number
            Optional<Order> dbOrderOpt = pFinishedExchOrderList.stream()
                    .filter(dbOrder -> dbOrder.getSeOrderId().equals(exchOrder.getOrderId())).findFirst();
            if (dbOrderOpt.isPresent()) {
                pFinishedExchOrderList.remove(dbOrderOpt.get());
            } else {
                Order order = SEOrderWrapper2StockExchangeOrderMapper
                        .INSTANCE.SEOrderWrapper2StockExchangeOrder(exchOrder, apiAccount);
                order.setExecuteTimestamp(null);
                newExchOrderList.add(order);
            }
        });

        finishedExchOrderList.addAll(pFinishedExchOrderList);
    }

    private void processNewExchOrders(final ApiAccount apiAccount,
                                      final List<Order> newExchOrderList) {
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

    private void processFinishedExchOrders(final ApiAccount apiAccount,
                                           final List<Order> finishedExchOrderList) {
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

    private void requestAndUpdateOrder(Order order, ApiAccount apiAccount) {
        OrderWrapper wrapper = apiAccount.getExchClient()// сделать метод запроса в апи аккауне
                .requestOrderDetails(order.getInstrument(), order.getSeOrderId());
        Order exchOrder = SEOrderWrapper2StockExchangeOrderMapper
                .INSTANCE.SEOrderWrapper2StockExchangeOrder(wrapper, order.getStockExchangeApiAccount());
        order.setExecuteTimestamp(exchOrder.getExecuteTimestamp());
        order.setState(exchOrder.getState());
        stockExchangeOrderService.save(order);
    }

}
