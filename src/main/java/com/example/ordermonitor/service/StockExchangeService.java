package com.example.ordermonitor.service;

import com.example.ordermonitor.model.StockExchange;
import com.example.ordermonitor.repository.StockExchangeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockExchangeService {

    private final StockExchangeRepository stockExchangeRepository;

    public StockExchangeService(StockExchangeRepository stockExchangeRepository) {
        this.stockExchangeRepository = stockExchangeRepository;
    }

    public List<StockExchange> getStockExchangeList() {
        return stockExchangeRepository.findAll();
    }

    public StockExchange saveStockExchange(StockExchange stockExchange) {
        return stockExchangeRepository.save(stockExchange);
    }

    public void deleteStockExchange(StockExchange stockExchange) {
        stockExchangeRepository.delete(stockExchange);
    }

}
