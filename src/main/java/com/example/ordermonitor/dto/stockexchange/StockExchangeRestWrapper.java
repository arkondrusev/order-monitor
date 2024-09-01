package com.example.ordermonitor.dto.stockexchange;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockExchangeRestWrapper {

    private final Long id;
    private final String name;

}