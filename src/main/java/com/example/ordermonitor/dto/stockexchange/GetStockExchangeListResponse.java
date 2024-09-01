package com.example.ordermonitor.dto.stockexchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStockExchangeListResponse {

    private List<StockExchangeRestWrapper> stockExchangeList;

}
