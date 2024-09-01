package com.example.ordermonitor.service;

import com.example.ordermonitor.dto.stockexchange.*;
import com.example.ordermonitor.mapper.StockExchange2StockExchangeRestWrapperMapper;
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

    public CreateStockExchangeResponse createStockExchange(CreateStockExchangeRequest request) {
        return null;
    }

    public GetStockExchangeListResponse getStockExchangeListWrapped() {
        return new GetStockExchangeListResponse(StockExchange2StockExchangeRestWrapperMapper
                .INSTANCE.stockExchange2StockExchangeRestWrapper(stockExchangeRepository.findAll()));
    }

    public UpdateStockExchangeResponse updateStockExchange(UpdateStockExchangeRequest request) {
        return null;
    }

    public DeleteStockExchangeResponse deleteStockExchange(DeleteStockExchangeRequest request) {
        return null;
    }

}
