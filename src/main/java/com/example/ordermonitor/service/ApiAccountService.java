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
        try {
            return null;
        } catch (Exception e) {
            return new CreateApiAccountResponse(RESPONSE_CODE_ERROR, e.getMessage());
        }
    }

    public UpdateApiAccountResponse updateAccount(UpdateApiAccountRequest request) {
        try {
            return null;
        } catch (Exception e) {
            return new UpdateApiAccountResponse(RESPONSE_CODE_ERROR, e.getMessage());
        }
    }

    public GetApiAccountListResponse getAccountList(GetApiAccountListRequest request) {
        try {
            return null;
        } catch (Exception e) {
            return new GetApiAccountListResponse(RESPONSE_CODE_ERROR, e.getMessage());
        }
    }

    public DeleteApiAccountResponse deleteAccount(DeleteApiAccountRequest request) {
        try {
            return null;
        } catch (Exception e) {
            return new DeleteApiAccountResponse(RESPONSE_CODE_ERROR, e.getMessage());
        }
    }

}
