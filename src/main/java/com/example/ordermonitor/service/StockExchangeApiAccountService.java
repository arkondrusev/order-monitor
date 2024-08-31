package com.example.ordermonitor.service;

import com.example.ordermonitor.model.StockExchange;
import com.example.ordermonitor.model.StockExchangeApiAccount;
import com.example.ordermonitor.repository.StockExchangeApiAccountRepository;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockExchangeApiAccountService {

    private final StockExchangeApiAccountRepository stockExchangeApiAccountRepository;

    public StockExchangeApiAccountService(StockExchangeApiAccountRepository stockExchangeApiAccountRepository) {
        this.stockExchangeApiAccountRepository = stockExchangeApiAccountRepository;
    }

    public List<StockExchangeApiAccount> getStockExchangeApiAccount(@Nullable StockExchange stockExchange) {
        if (stockExchange == null) {
            return stockExchangeApiAccountRepository.findAll();
        } else {
            return stockExchangeApiAccountRepository.findAllByStockExchange(stockExchange);
        }
    }

    public StockExchangeApiAccount saveStockExchangeApiAccount(StockExchangeApiAccount stockExchangeApiAccount) {
        return stockExchangeApiAccountRepository.save(stockExchangeApiAccount);
    }

    public void deleteStockExchangeApiAccount(StockExchangeApiAccount stockExchangeApiAccount) {
        stockExchangeApiAccountRepository.delete(stockExchangeApiAccount);
    }

}
