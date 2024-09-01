package com.example.ordermonitor.stockexch.client;

import com.example.ordermonitor.dto.order.OrderWrapper;

import java.util.List;

public interface ExchClient {

    List<OrderWrapper> requestOrderList();
    OrderWrapper requestOrderDetails(String instId, String orderId);

}
