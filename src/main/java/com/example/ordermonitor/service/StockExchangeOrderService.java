package com.example.ordermonitor.service;

import com.example.ordermonitor.dto.SEActiveOrderReceiptWrapper;
import com.example.ordermonitor.model.StockExchange;
import com.example.ordermonitor.model.StockExchangeOrder;
import com.example.ordermonitor.repository.StockExchangeOrderRepository;
import com.example.ordermonitor.repository.StockExchangeRepository;
import com.example.ordermonitor.stockexch.okx.OkxClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockExchangeOrderService {

    private final StockExchangeOrderRepository stockExchangeOrderRepository;
    private final StockExchangeRepository stockExchangeRepository;

    private List<StockExchangeOrder> dbOrderList;
    private final OkxClient okxClient;
    private final StockExchange okxExchange;

    private static final Long STOCK_EXCHANGE_OKX_ID = 1L;
    private static final String STATE_LIVE = "live";

    public StockExchangeOrderService(OkxClient okxClient, StockExchangeOrderRepository stockExchangeOrderRepository,
                                     StockExchangeRepository stockExchangeRepository) {
        this.stockExchangeOrderRepository = stockExchangeOrderRepository;
        this.stockExchangeRepository = stockExchangeRepository;
        this.okxClient = okxClient;
        this.okxExchange = stockExchangeRepository.findById(STOCK_EXCHANGE_OKX_ID).get();

        // 1 запрос для коллекции из БД при старте
        dbOrderList = stockExchangeOrderRepository.findByState(STATE_LIVE);
    }

    public void checkExchOrders() {
        List<SEActiveOrderReceiptWrapper> exchOrderList = okxClient.requestExchangeOrders();

        List<SEActiveOrderReceiptWrapper> newExchOrderList = new ArrayList<>();
        List<StockExchangeOrder> finishedExchOrderList = new ArrayList<>();

        findNewAndFinishedOrders(exchOrderList, newExchOrderList, finishedExchOrderList);

        processNewExchOrders(newExchOrderList);
        processFinishedExchOrders(finishedExchOrderList);
    }

    private void findNewAndFinishedOrders(final List<SEActiveOrderReceiptWrapper> exchOrderList,
                                         final List<SEActiveOrderReceiptWrapper> newExchOrderList,
                                         final List<StockExchangeOrder> finishedExchOrderList) {
        final List<StockExchangeOrder> pFinishedExchOrderList = new ArrayList<>(dbOrderList);
        exchOrderList.forEach(e -> {
            if (dbOrderList.contains(e)) {
                pFinishedExchOrderList.remove(e);
            } else {
                newExchOrderList.add(new SEActiveOrderReceiptWrapper(e.getOrderId(),
                        e.getOrderType(), e.getInstrument(), e.getTradeSide(), e.getQuantity(),
                        e.getPrice(), e.getOpenTimestamp(), e.getState()));
            }
        });

        pFinishedExchOrderList.forEach(e->finishedExchOrderList.add(e));
    }

    private void processNewExchOrders(final List<SEActiveOrderReceiptWrapper> newExchOrderWrList) {
        // если на бирже есть а в БД нет, то сохранить в БД и послать уведомление в ТГ
        if (newExchOrderWrList.isEmpty()) {
            return;
        }
        newExchOrderWrList.forEach(e -> {
            if (dbOrderList.stream().filter(n -> n.getSeOrderId().equals(e.getOrderId()))
                    .findFirst().isEmpty()) {
                dbOrderList.add(createStockExchangeOrder(e));
                //send TG message
            }
        });
    }

    public StockExchangeOrder createStockExchangeOrder(SEActiveOrderReceiptWrapper orderWrapper) {
        StockExchangeOrder newOrder = new StockExchangeOrder(null, okxExchange, orderWrapper.getOrderId(),
                orderWrapper.getOrderType(), orderWrapper.getInstrument(), orderWrapper.getTradeSide(),
                new BigDecimal(orderWrapper.getQuantity()), new BigDecimal(orderWrapper.getPrice()),
                calcZonedDateTime(orderWrapper.getOpenTimestamp()), null, orderWrapper.getState());
        return stockExchangeOrderRepository.save(newOrder);
    }

    private ZonedDateTime calcZonedDateTime(String epochSecondsString) {
        Long openTimestamp = Long.parseLong(epochSecondsString);
        Instant instantTimestamp = Instant.ofEpochSecond(openTimestamp);
        return ZonedDateTime.ofInstant(instantTimestamp, ZoneId.of("UTC"));
    }

    private void processFinishedExchOrders(final List<StockExchangeOrder> finishedExchOrderList) {
        // если в БД есть а на бирже нет, то запросить по дельте статус и обновить статусы в БД,
        // после чего послать уведомление в ТГ
    }

}
