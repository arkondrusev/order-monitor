package com.example.ordermonitor.service;

import com.example.ordermonitor.model.StockExchangeApiAccount;
import com.example.ordermonitor.model.StockExchangeOrder;
import com.example.ordermonitor.repository.StockExchangeOrderRepository;
import com.example.ordermonitor.repository.StockExchangeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockExchangeOrderService {

    private final StockExchangeOrderRepository stockExchangeOrderRepository;

    private static final String ORDER_STATE_LIVE = "live";

    public StockExchangeOrderService(StockExchangeOrderRepository stockExchangeOrderRepository,
                                     StockExchangeRepository stockExchangeRepository) {
        this.stockExchangeOrderRepository = stockExchangeOrderRepository;
    }

    public List<StockExchangeOrder> getLiveStockExchangeOrderList(StockExchangeApiAccount apiAccount) {
        List<StockExchangeOrder> liveOrderList = stockExchangeOrderRepository.findAllByStockExchangeApiAccount(apiAccount);
        return liveOrderList.stream().filter(e -> e.getState().equals(ORDER_STATE_LIVE)).collect(Collectors.toList());
    }

    public StockExchangeOrder save(StockExchangeOrder stockExchangeOrder) {
        return stockExchangeOrderRepository.save(stockExchangeOrder);
    }

}
