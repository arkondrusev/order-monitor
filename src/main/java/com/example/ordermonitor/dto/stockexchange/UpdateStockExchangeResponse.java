package com.example.ordermonitor.dto.stockexchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStockExchangeResponse {

    private Integer responseCode;
    private String responseMessage;
    private Long id;
    private String name;

    public UpdateStockExchangeResponse(Integer responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.id = null;
        this.name = null;
    }

}
