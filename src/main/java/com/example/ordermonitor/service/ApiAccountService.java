package com.example.ordermonitor.service;

import com.example.ordermonitor.dto.account.*;
import com.example.ordermonitor.model.ApiAccount;
import com.example.ordermonitor.model.StockExchange;
import com.example.ordermonitor.repository.ApiAccountRepository;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiAccountService implements IRestService {

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

    public CreateApiAccountResponse createAccount(CreateApiAccountRequest request) {
        return null;
    }

    public UpdateApiAccountResponse updateAccount(UpdateApiAccountRequest request) {
        return null;
    }

    public GetApiAccountListResponse getAccountList(GetApiAccountListRequest request) {
        return null;
    }

    public DeleteApiAccountResponse deleteAccount(DeleteApiAccountRequest request) {
        return null;
    }

}
