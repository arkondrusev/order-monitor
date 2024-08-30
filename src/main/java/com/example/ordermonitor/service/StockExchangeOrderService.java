package com.example.ordermonitor.service;

import com.example.ordermonitor.dto.SEOrderWrapper;
import com.example.ordermonitor.mapper.SEOrderWrapper2StockExchangeOrderMapper;
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
import java.util.Optional;

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
        List<SEOrderWrapper> exchOrderList = okxClient.requestExchangeOrders();

        List<StockExchangeOrder> newExchOrderList = new ArrayList<>();
        List<StockExchangeOrder> finishedExchOrderList = new ArrayList<>();

        findNewAndFinishedOrders(exchOrderList, newExchOrderList, finishedExchOrderList);

        processNewExchOrders(newExchOrderList);
        processFinishedExchOrders(finishedExchOrderList);
    }

    private void findNewAndFinishedOrders(final List<SEOrderWrapper> exchOrderList,
                                         final List<StockExchangeOrder> newExchOrderList,
                                         final List<StockExchangeOrder> finishedExchOrderList) {
        final List<StockExchangeOrder> pFinishedExchOrderList = new ArrayList<>(dbOrderList);
        exchOrderList.forEach(exchOrder -> {
            // compare by exchange order number
            Optional<StockExchangeOrder> dbOrderOpt = dbOrderList.stream().filter(dbOrder -> dbOrder.getSeOrderId()
                    .equals(exchOrder.getOrderId())).findFirst();
            if (dbOrderOpt.isPresent()) {
                pFinishedExchOrderList.remove(dbOrderOpt.get());
            } else {
                newExchOrderList.add(SEOrderWrapper2StockExchangeOrderMapper
                        .INSTANCE.SEOrderWrapper2StockExchangeOrder(exchOrder));
            }
        });

        pFinishedExchOrderList.forEach(e->finishedExchOrderList.add(e));
    }

    private void processNewExchOrders(final List<StockExchangeOrder> newExchOrderWrList) {
        // если на бирже есть а в БД нет, то сохранить в БД и послать уведомление в ТГ
        if (newExchOrderWrList.isEmpty()) {
            return;
        }
        newExchOrderWrList.forEach(e -> {
            // compare by exchange order number
            if (dbOrderList.stream().filter(n -> n.getSeOrderId().equals(e.getOrderId()))
                    .findFirst().isEmpty()) {
                dbOrderList.add(createStockExchangeOrder(e));
                //send TG message
            }
        });
    }

    private void processFinishedExchOrders(final List<StockExchangeOrder> finishedExchOrderList) {
        // если в БД есть а на бирже нет, то запросить по дельте статус и обновить статусы в БД,
        // после чего послать уведомление в ТГ
        if (finishedExchOrderList.isEmpty()) {
            return;
        }
        finishedExchOrderList.forEach(e -> {

            // send TG message
        });
    }

    private StockExchangeOrder createStockExchangeOrder(SEOrderWrapper orderWrapper) {
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

}
