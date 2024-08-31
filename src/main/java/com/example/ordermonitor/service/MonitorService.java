package com.example.ordermonitor.service;

import com.example.ordermonitor.model.StockExchange;
import com.example.ordermonitor.model.StockExchangeApiAccount;
import com.example.ordermonitor.model.StockExchangeOrder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MonitorService {

    private final StockExchangeService stockExchangeService;
    private final StockExchangeApiAccountService stockExchangeApiAccountService;
    private final StockExchangeOrderService stockExchangeOrderService;

    private final List<StockExchange> stockExchangeList = new ArrayList<>();
    private final Map<StockExchange, List<StockExchangeApiAccount>> stockExchangeApiAccountList = new HashMap<>();
    private final Map<StockExchangeApiAccount, List<StockExchangeOrder>> stockExchangeDBOrderList = new HashMap<>();

    public MonitorService(StockExchangeService stockExchangeService,
                          StockExchangeApiAccountService stockExchangeApiAccountService,
                          StockExchangeOrderService stockExchangeOrderService) {
        this.stockExchangeService = stockExchangeService;
        this.stockExchangeApiAccountService = stockExchangeApiAccountService;
        this.stockExchangeOrderService = stockExchangeOrderService;

        initData();
    }

    private void initData() {
        stockExchangeService.getStockExchangeList().forEach(e -> {
            stockExchangeList.add(e);
            stockExchangeList.forEach(this::initStockExchangeDate);
        });
    }

    private void initStockExchangeDate(StockExchange se) {
        List<StockExchangeApiAccount> seApiAccountList = stockExchangeApiAccountService.getStockExchangeApiAccount(se);
        stockExchangeApiAccountList.put(se, seApiAccountList);
        seApiAccountList.forEach(acc -> stockExchangeDBOrderList
                .put(acc, stockExchangeOrderService.getStockExchangeOrderList(acc)));
    }

    @Scheduled(fixedDelay = 10000)
    public void scheduleCheckOrders() {
        checkExchangesOrders();
    }

    private void checkExchangesOrders() {
        
    }

}
