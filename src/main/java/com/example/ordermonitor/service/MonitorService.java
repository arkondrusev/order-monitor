package com.example.ordermonitor.service;

import com.example.ordermonitor.dto.monitor.GetOrderCheckPeriodResponse;
import com.example.ordermonitor.dto.monitor.SetOrderCheckPeriodRequest;
import com.example.ordermonitor.dto.monitor.SetOrderCheckPeriodResponse;
import com.example.ordermonitor.dto.order.ExchOrderWrapper;
import com.example.ordermonitor.mapper.ExchOrderWrapper2OrderMapper;
import com.example.ordermonitor.model.ApiAccount;
import com.example.ordermonitor.model.Order;
import com.example.ordermonitor.model.StockExchange;
import com.example.ordermonitor.stockexch.ExchConfig;
import com.example.ordermonitor.stockexch.client.OkxClient;
import com.example.ordermonitor.telegram.TelegramBot;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MonitorService implements IRestService {

    private final StockExchangeService stockExchangeService;
    private final ApiAccountService apiAccountService;
    private final OrderService stockExchangeOrderService;
    private final TelegramBot telegramBot;
    private final Environment env;

    private final List<StockExchange> stockExchangeList = new ArrayList<>();
    private final Map<StockExchange, List<ApiAccount>> stockExchangeApiAccountList = new HashMap<>();
    private final Map<ApiAccount, List<Order>> stockExchangeDBOrderList = new HashMap<>();

    private AtomicInteger orderCheckSchedulerDelay = new AtomicInteger(10000);

    public MonitorService(StockExchangeService stockExchangeService,
                          ApiAccountService apiAccountService,
                          OrderService stockExchangeOrderService,
                          TelegramBot telegramBot,
                          Environment env) {
        this.stockExchangeService = stockExchangeService;
        this.apiAccountService = apiAccountService;
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
        List<ApiAccount> seApiAccountList = apiAccountService.getApiAccountList(se);
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

    public AtomicInteger getOrderCheckSchedulerDelay() {
        return orderCheckSchedulerDelay;
    }

    public void checkExchangesOrders() {
        stockExchangeList.forEach(se -> stockExchangeApiAccountList.get(se).forEach(acc -> {
            List<ExchOrderWrapper> exchOrderList = acc.getExchClient().requestOrderList();
            List<Order> newExchOrderList = new ArrayList<>();
            List<Order> finishedExchOrderList = new ArrayList<>();
            findNewAndFinishedOrders(acc, exchOrderList, newExchOrderList, finishedExchOrderList);

            processNewExchOrders(acc, newExchOrderList);
            processFinishedExchOrders(acc, finishedExchOrderList);
        }));
    }

    private void findNewAndFinishedOrders(final ApiAccount apiAccount,
                                        final List<ExchOrderWrapper> exchOrderList,
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
                Order order = ExchOrderWrapper2OrderMapper
                        .INSTANCE.OrderWrapper2Order(exchOrder, apiAccount);
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
                telegramBot.sendMessage("376653873", getNotificationMessage(e, false));
            } catch (TelegramApiException ex) {
                System.out.println(ex);
            }
        });
    }

    private String getNotificationMessage(Order order, boolean isClosed) {
        StringBuilder sb = new StringBuilder();
        if (!isClosed) {
            sb.append("Order placed:\n");
        } else {
            sb.append("Order finished:\n");
        }
        sb.append("exchange: ").append(order.getApiAccount().getStockExchange().getName())
                .append(" order №: ").append(order.getSeOrderId()).append(" status: ").append(order.getState())
                .append("\n").append(order.getInstrument()).append(" ").append(order.getTradeSide())
                .append(" qty: ").append(order.getQuantity()).append(" price: ").append(order.getPrice()).append("\n")
                .append("placed: ").append(order.getOpenTimestamp());
        if (isClosed) {
            sb.append("\nclosed: ").append(order.getExecuteTimestamp());
        }
        return sb.toString();
    }

    private void processFinishedExchOrders(final ApiAccount apiAccount,
                                           final List<Order> finishedExchOrderList) {
        // если в БД есть а на бирже нет, то запросить по дельте статус и обновить статусы в БД,
        // после чего послать уведомление в ТГ
        finishedExchOrderList.forEach(e -> {
            requestAndUpdateOrder(e, apiAccount);
            stockExchangeDBOrderList.get(apiAccount).remove(e);
            try {
                telegramBot.sendMessage("376653873", getNotificationMessage(e, true));
            } catch (Exception ex) {
                System.out.println(ex);
            }
        });
    }

    private void requestAndUpdateOrder(Order order, ApiAccount apiAccount) {
        ExchOrderWrapper wrapper = apiAccount.getExchClient()// сделать метод запроса в апи аккауне
                .requestOrderDetails(order.getInstrument(), order.getSeOrderId());
        Order exchOrder = ExchOrderWrapper2OrderMapper
                .INSTANCE.OrderWrapper2Order(wrapper, order.getApiAccount());
        order.setExecuteTimestamp(exchOrder.getExecuteTimestamp());
        order.setState(exchOrder.getState());
        stockExchangeOrderService.save(order);
    }

    public SetOrderCheckPeriodResponse setOrderCheckPeriod(SetOrderCheckPeriodRequest request) {
        try {
            checkSetOrderCheckPeriod(request);
            orderCheckSchedulerDelay.set(request.getOrderCheckPeriodMillis());
            return new SetOrderCheckPeriodResponse(RESPONSE_CODE_OK, RESPONSE_MESSAGE_OK);
        } catch (Exception e) {
            return new SetOrderCheckPeriodResponse(RESPONSE_CODE_ERROR, e.getMessage());
        }
    }

    private void checkSetOrderCheckPeriod(SetOrderCheckPeriodRequest request) {
        if (request.getOrderCheckPeriodMillis() == null) {
            throw new IllegalArgumentException("OrderCheckPeriodMillis is required");
        }
    }

    public GetOrderCheckPeriodResponse getOrderCheckPeriod() {
        try {
            return new GetOrderCheckPeriodResponse(RESPONSE_CODE_OK, RESPONSE_MESSAGE_OK,
                    orderCheckSchedulerDelay.get());
        } catch (Exception e) {
            return new GetOrderCheckPeriodResponse(RESPONSE_CODE_ERROR, e.getMessage(), null);
        }
    }

}
