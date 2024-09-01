package com.example.ordermonitor.stockexch.client;

import com.example.ordermonitor.dto.order.ExchOrderWrapper;

import java.util.List;

public interface ExchClient {

    List<ExchOrderWrapper> requestOrderList();
    ExchOrderWrapper requestOrderDetails(String instId, String orderId);

}
