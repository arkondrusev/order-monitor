package com.example.ordermonitor.controller;

import com.example.ordermonitor.dto.stockexchange.*;
import com.example.ordermonitor.service.StockExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("stock-exchange")
@RestController
@RequiredArgsConstructor
public class StockExchangeController {

    private final StockExchangeService stockExchangeService;

    @PostMapping("create")
    public CreateStockExchangeResponse createStock(@RequestBody CreateStockExchangeRequest request) {
        return stockExchangeService.createStockExchange(request);
    }

    @GetMapping("get-list")
    public GetStockExchangeListResponse getStockList() {
        return stockExchangeService.getStockExchangeListWrapped();
    }

    @PutMapping("update")
    public UpdateStockExchangeResponse updateStock(@RequestBody UpdateStockExchangeRequest request) {
        return stockExchangeService.updateStockExchange(request);
    }

    @DeleteMapping("delete")
    public DeleteStockExchangeResponse deleteStock(@RequestBody DeleteStockExchangeRequest request) {
        return stockExchangeService.deleteStockExchange(request);
    }

}
