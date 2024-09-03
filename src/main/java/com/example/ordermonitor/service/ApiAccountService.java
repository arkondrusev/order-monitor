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

    public List<ApiAccount> getApiAccountList() {
        return apiAccountRepository.findAll();
    }

    public List<ApiAccount> getApiAccountList(@Nullable StockExchange stockExchange) {
        return apiAccountRepository.findAllByStockExchange(stockExchange);
    }

    public ApiAccount saveApiAccount(ApiAccount stockExchangeApiAccount) {
        return apiAccountRepository.save(stockExchangeApiAccount);
    }

    public void deleteStockExchange(ApiAccount stockExchangeApiAccount) {
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
            newApiAccount = saveApiAccount(newApiAccount);
            return dtoMapper.apiAccount2CreateApiAccountResponse(newApiAccount, RESPONSE_CODE_OK, RESPONSE_MESSAGE_OK);
        } catch (Exception e) {
            return new CreateApiAccountResponse(RESPONSE_CODE_ERROR, e.getMessage());
        }
    }

    private void checkCreateApiAccountParams(CreateApiAccountRequest request) {
        if (request.getAccountName() == null) {
            throw new IllegalArgumentException("Account name is required");
        }
        if (request.getStockExchangeId() == null) {
            throw new IllegalArgumentException("Stock exchange id is required");
        }
    }

    public UpdateApiAccountResponse updateAccount(UpdateApiAccountRequest request) {
        try {
            checkUpdateApiAccountParams(request);
            Optional<ApiAccount> apiAccountOpt = apiAccountRepository.findById(request.getAccountId());
            if (apiAccountOpt.isEmpty()) {
                throw new IllegalArgumentException("Api account not found");
            }
            ApiAccount apiAccount = apiAccountOpt.get();
            apiAccount.setTelegramUsername(request.getTelegramUsername());
            apiAccount = saveApiAccount(apiAccount);
            return dtoMapper.apiAccount2UpdateApiAccountResponse(dtoMapper
                    .apiAccount2ApiAccountRestWrapper(apiAccount), RESPONSE_CODE_OK, RESPONSE_MESSAGE_OK);
        } catch (Exception e) {
            return new UpdateApiAccountResponse(RESPONSE_CODE_ERROR, e.getMessage());
        }
    }

    private void checkUpdateApiAccountParams(UpdateApiAccountRequest request) {
        if (request.getAccountId() == null) {
            throw new IllegalArgumentException("Account id is required");
        }
    }

    public GetApiAccountListResponse getAccountList(GetApiAccountListRequest request) {
        try {
            checkGetAccountListParams(request);
            Optional<StockExchange> stockExchangeOpt = stockExchangeRepository.findById(request.getStockExchangeId());
            if (stockExchangeOpt.isEmpty()) {
                throw new IllegalArgumentException("Stock exchange not found");
            }
            List<ApiAccount> apiAccountList = apiAccountRepository.findAllByStockExchange(stockExchangeOpt.get());
            List<ApiAccountRestWrapper> apiAccountWrapperList = dtoMapper
                    .apiAccount2ApiAccountRestWrapper(apiAccountList);
            return new GetApiAccountListResponse(RESPONSE_CODE_OK, RESPONSE_MESSAGE_OK, apiAccountWrapperList);
        } catch (Exception e) {
            return new GetApiAccountListResponse(RESPONSE_CODE_ERROR, e.getMessage());
        }
    }

    private void checkGetAccountListParams(GetApiAccountListRequest request) {
        if (request.getStockExchangeId() == null) {
            throw new IllegalArgumentException("Stock exchange id is required");
        }
    }

    public DeleteApiAccountResponse deleteAccount(DeleteApiAccountRequest request) {
        try {
            checkDeleteApiAccountParams(request);
            Optional<ApiAccount> apiAccountOpt = apiAccountRepository.findById(request.getAccountId());
            if (apiAccountOpt.isEmpty()) {
                throw new IllegalArgumentException("Api account not found");
            }
            deleteStockExchange(apiAccountOpt.get());
            return new DeleteApiAccountResponse(RESPONSE_CODE_OK, RESPONSE_MESSAGE_OK);
        } catch (Exception e) {
            return new DeleteApiAccountResponse(RESPONSE_CODE_ERROR, e.getMessage());
        }
    }

    private void checkDeleteApiAccountParams(DeleteApiAccountRequest request) {
        if (request.getAccountId() == null) {
            throw new IllegalArgumentException("Account id is required");
        }
    }

}
