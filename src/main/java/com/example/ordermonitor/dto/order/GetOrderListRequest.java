package com.example.ordermonitor.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderListRequest {

    private Long apiAccountId;
    private String instId;
    private String state;
    private ZonedDateTime activeBegin;
    private ZonedDateTime activeEnd;

}
