package com.example.ordermonitor.stockexch.client;

import com.example.ordermonitor.dto.SEOrderWrapper;

import java.util.List;

public interface ExchClient {

    List<SEOrderWrapper> requestOrderList();
    SEOrderWrapper requestOrderDetails(String instId, String orderId);

}
