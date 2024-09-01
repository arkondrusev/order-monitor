package com.example.ordermonitor.dto.stockexchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStockExchangeListResponse {

    private Integer responseCode;
    private String responseMessage;
    private List<StockExchangeRestWrapper> stockExchangeList;

    public GetStockExchangeListResponse(Integer responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.stockExchangeList = new ArrayList<>();
    }

}
