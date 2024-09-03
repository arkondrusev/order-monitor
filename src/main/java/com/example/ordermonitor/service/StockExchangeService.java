package com.example.ordermonitor.service;

import com.example.ordermonitor.dto.stockexchange.*;
import com.example.ordermonitor.mapper.StockExchange2DTOMapper;
import com.example.ordermonitor.model.StockExchange;
import com.example.ordermonitor.repository.StockExchangeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockExchangeService implements IRestService {

    private final StockExchangeRepository stockExchangeRepository;

    private final StockExchange2DTOMapper dtoMapper = StockExchange2DTOMapper.INSTANCE;

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

    public CreateStockExchangeResponse createStockExchange(CreateStockExchangeRequest request) {
        try {
            checkCreateStockExchangeParams(request);
            StockExchange newStockExchange = dtoMapper.createStockExchangeRequest2StockExchange(request);
            newStockExchange = saveStockExchange(newStockExchange);
            return dtoMapper.stockExchange2CreateStockExchangeResponse(newStockExchange,
                    RESPONSE_CODE_OK, RESPONSE_MESSAGE_OK);
        } catch (Exception e) {
            return new CreateStockExchangeResponse(RESPONSE_CODE_ERROR, e.getMessage());
        }
    }

    public void checkCreateStockExchangeParams(CreateStockExchangeRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
    }

    public GetStockExchangeListResponse getStockExchangeListWrapped() {
        try {
            GetStockExchangeListResponse getStockExchangeListResponse =
                    new GetStockExchangeListResponse(RESPONSE_CODE_OK, RESPONSE_MESSAGE_OK,
                            dtoMapper.stockExchange2StockExchangeRestWrapper(stockExchangeRepository.findAll()));
            return getStockExchangeListResponse;
        } catch (Exception e) {
            return new GetStockExchangeListResponse(RESPONSE_CODE_ERROR, e.getMessage());
        }
    }

    public UpdateStockExchangeResponse updateStockExchange(UpdateStockExchangeRequest request) {
        try {
            checkUpdateStockExchangeParams(request);
            StockExchange updatedStockExchange = dtoMapper.updateStockExchangeRequest2StockExchange(request);
            updatedStockExchange = saveStockExchange(updatedStockExchange);
            return dtoMapper.stockExchange2UpdateStockExchangeResponse(updatedStockExchange,
                    RESPONSE_CODE_OK, RESPONSE_MESSAGE_OK);
        } catch (Exception e) {
            return new UpdateStockExchangeResponse(RESPONSE_CODE_ERROR, e.getMessage());
        }
    }

    private void checkUpdateStockExchangeParams(UpdateStockExchangeRequest request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Id is required");
        }
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
    }

    public DeleteStockExchangeResponse deleteStockExchange(DeleteStockExchangeRequest request) {
        try {
            checkDeleteStockExchangeParams(request);
            Optional<StockExchange> stockExchangeOpt = stockExchangeRepository.findById(request.getId());
            if (stockExchangeOpt.isEmpty()) {
                throw new IllegalArgumentException("Stock exchange not found");
            }
            stockExchangeRepository.delete(stockExchangeOpt.get());
            return new DeleteStockExchangeResponse(RESPONSE_CODE_OK, RESPONSE_MESSAGE_OK);
        } catch (Exception e) {
            return new DeleteStockExchangeResponse(RESPONSE_CODE_ERROR, e.getMessage());
        }
    }

    private void checkDeleteStockExchangeParams(DeleteStockExchangeRequest request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Id is required");
        }
    }

    public SetOrderCheckPeriodResponse setOrderCheckPeriod(SetOrderCheckPeriodRequest request) {
        return null;
    }

}
