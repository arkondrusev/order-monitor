package com.example.ordermonitor.service;

import com.example.ordermonitor.dto.account.*;
import com.example.ordermonitor.mapper.ApiAccount2DTOMapper;
import com.example.ordermonitor.model.ApiAccount;
import com.example.ordermonitor.model.StockExchange;
import com.example.ordermonitor.repository.ApiAccountRepository;
import com.example.ordermonitor.repository.StockExchangeRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiAccountService implements IRestService {

    private final ApiAccountRepository apiAccountRepository;
    private final StockExchangeRepository stockExchangeRepository;

    private final ApiAccount2DTOMapper dtoMapper = ApiAccount2DTOMapper.INSTANCE;

    public List<ApiAccount> getApiAccountList(@Nullable StockExchange stockExchange) {
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
            checkCreateApiAccountParams(request);
            Optional<StockExchange> stockExchangeOpt = stockExchangeRepository.findById(request.getStockExchangeId());
            if (stockExchangeOpt.isEmpty()) {
                throw new IllegalArgumentException("StockExchange not found");
            }
            ApiAccount newApiAccount = dtoMapper.createApiAccountRequest2ApiAccount(request, stockExchangeOpt.get());
            newApiAccount = apiAccountRepository.save(newApiAccount);
            return dtoMapper.apiAccount2CreateApiAccountResponse(newApiAccount, RESPONSE_CODE_OK, RESPONSE_MESSAGE_OK);
        } catch (Exception e) {
            return new CreateApiAccountResponse(RESPONSE_CODE_ERROR, e.getMessage());
        }
    }

    private void checkCreateApiAccountParams(CreateApiAccountRequest request) {
        if (request.getAccountName() == null) {
            throw new IllegalArgumentException("AccountName is required");
        }
        if (request.getStockExchangeId() == null) {
            throw new IllegalArgumentException("StockExchangeId is required");
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
