package com.example.ordermonitor.dto.monitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderCheckPeriodResponse {

    private Integer responseCode;
    private String responseMessage;
    private Integer orderCheckPeriodMillis;

}
