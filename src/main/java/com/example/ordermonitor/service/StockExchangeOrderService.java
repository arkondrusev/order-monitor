package com.example.ordermonitor.service;

import com.example.ordermonitor.dto.SEActiveOrderReceiptWrapper;
import com.example.ordermonitor.model.StockExchangeOrder;
import com.example.ordermonitor.repository.StockExchangeOrderRepository;
import com.example.ordermonitor.stockexch.okx.OkxClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockExchangeOrderService {

    private StockExchangeOrderRepository stockExchangeOrderRepository;

    private List<StockExchangeOrder> dbOrderList;

    private final OkxClient okxClient;

    private static final String STATE_LIVE = "live";

    public StockExchangeOrderService(OkxClient okxClient, StockExchangeOrderRepository stockExchangeOrderRepository) {
        this.okxClient = okxClient;
        this.stockExchangeOrderRepository = stockExchangeOrderRepository;
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
        List<StockExchangeOrder> newExchOrderList = new ArrayList<>();
        newExchOrderWrList.forEach(e -> {
            StockExchangeOrder newOrder = new StockExchangeOrder();
            newExchOrderList.add(newOrder);
            dbOrderList.add(newOrder);
        });
        stockExchangeOrderRepository.saveAll(newExchOrderList);
    }

    private void processFinishedExchOrders(final List<StockExchangeOrder> finishedExchOrderList) {
        // если в БД есть а на бирже нет, то запросить по дельте статус и обновить статусы в БД,
        // после чего послать уведомление в ТГ
    }

}
