package com.example.ordermonitor.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateApiAccountRequest {

    private String accountName;
    private Long StockExchangeId;
    private String telegramUsername;

}
