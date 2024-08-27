package com.example.ordermonitor.monitor;

import com.example.ordermonitor.service.StockExchangeOrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExchMonitor {

    private final StockExchangeOrderService seOrderService;

    public ExchMonitor(StockExchangeOrderService seOrderService) {
        this.seOrderService = seOrderService;
    }

    @Scheduled(fixedDelay = 10000)
    public void scheduleCheckOrders() {
        seOrderService.checkExchOrders();
    }

}
