package com.example.ordermonitor.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderRestWrapper {

    private final String orderId;
    private final Long apiAccountId;
    private final String orderType;
    private final String instrument;
    private final String tradeSide;
    private final String quantity;
    private final String price;
    private final String openTimestamp;
    private final String closeTimestamp;
    private final String state;

}
