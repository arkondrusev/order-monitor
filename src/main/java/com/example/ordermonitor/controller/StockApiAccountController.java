package com.example.ordermonitor.controller;

import com.example.ordermonitor.service.StockExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("stock-account")
@RestController
@RequiredArgsConstructor
public class StockApiAccountController {

    private final StockExchangeService stockService;

    @PostMapping("create")
    public CreateStockResponse createStock(@RequestBody CreateStockRequest request) {
        return stockService.saveStockExchange();
    }

    @GetMapping("get-list")
    public GetStockListResponse getStockList() {
        return stockService.getStockExchangeList();
    }

    @PutMapping("update")
    public UpdateStockResponse updateStock(@RequestBody UpdateStockRequest request) {
        return stockService.updateStock(request);
    }

    @DeleteMapping("delete")
    public DeleteStockResponse deleteStock(@RequestBody DeleteStockRequest request) {
        return stockService.deleteStockExchange(request);
    }


}
