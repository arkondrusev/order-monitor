package com.example.ordermonitor.dto.stockexchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetOrderCheckPeriodResponse {

    private Integer responseCode;
    private String responseMessage;

}
