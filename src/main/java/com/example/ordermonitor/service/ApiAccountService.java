package com.example.ordermonitor.service;

import com.example.ordermonitor.dto.account.*;
import com.example.ordermonitor.model.ApiAccount;
import com.example.ordermonitor.model.StockExchange;
import com.example.ordermonitor.repository.ApiAccountRepository;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiAccountService {

    private final ApiAccountRepository apiAccountRepository;

    public ApiAccountService(ApiAccountRepository apiAccountRepository) {
        this.apiAccountRepository = apiAccountRepository;
    }

    public List<ApiAccount> getStockExchangeApiAccount(@Nullable StockExchange stockExchange) {
        if (stockExchange == null) {
            return apiAccountRepository.findAll();
        } else {
            return apiAccountRepository.findAllByStockExchange(stockExchange);
        }
    }

    public ApiAccount saveStockExchangeApiAccount(ApiAccount stockExchangeApiAccount) {
        return apiAccountRepository.save(stockExchangeApiAccount);
    }

    public void deleteStockExchangeApiAccount(ApiAccount stockExchangeApiAccount) {
        apiAccountRepository.delete(stockExchangeApiAccount);
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
