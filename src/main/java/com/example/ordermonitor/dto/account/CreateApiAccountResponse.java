package com.example.ordermonitor.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateApiAccountResponse {

    private Integer responseCode;
    private String responseMessage;
    private Long accountId;
    private String accountName;
    private Long stockExchangeId;
    private String telegramUsername;

}
