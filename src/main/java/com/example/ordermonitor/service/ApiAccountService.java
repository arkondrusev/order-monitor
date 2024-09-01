package com.example.ordermonitor.service;

import com.example.ordermonitor.dto.account.*;
import com.example.ordermonitor.model.ApiAccount;
import com.example.ordermonitor.model.StockExchange;
import com.example.ordermonitor.repository.StockExchangeApiAccountRepository;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiAccountService {

    private final StockExchangeApiAccountRepository stockExchangeApiAccountRepository;

    public ApiAccountService(StockExchangeApiAccountRepository stockExchangeApiAccountRepository) {
        this.stockExchangeApiAccountRepository = stockExchangeApiAccountRepository;
    }

    public List<ApiAccount> getStockExchangeApiAccount(@Nullable StockExchange stockExchange) {
        if (stockExchange == null) {
            return stockExchangeApiAccountRepository.findAll();
        } else {
            return stockExchangeApiAccountRepository.findAllByStockExchange(stockExchange);
        }
    }

    public ApiAccount saveStockExchangeApiAccount(ApiAccount stockExchangeApiAccount) {
        return stockExchangeApiAccountRepository.save(stockExchangeApiAccount);
    }

    public void deleteStockExchangeApiAccount(ApiAccount stockExchangeApiAccount) {
        stockExchangeApiAccountRepository.delete(stockExchangeApiAccount);
    }

    public CreateAccountResponse createAccount(CreateAccountRequest request) {
        return null;
    }

    public UpdateAccountResponse updateAccount(UpdateAccountRequest request) {
        return null;
    }

    public GetAccountListResponse getAccountList(GetAccountListRequest request) {
        return null;
    }

    public DeleteAccountResponse deleteAccount(DeleteAccountRequest request) {
        return null;
    }

}
