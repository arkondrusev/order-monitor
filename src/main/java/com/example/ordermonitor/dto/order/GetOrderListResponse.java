package com.example.ordermonitor.dto.order;

import com.example.ordermonitor.dto.OrderWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderListResponse {

    private Integer responseCode;
    private String responseMessage;
    private List<OrderWrapper> orderList;

}
