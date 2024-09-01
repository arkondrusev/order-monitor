package com.example.ordermonitor.stockexch.client;

import com.example.ordermonitor.dto.SEOrderWrapper;
import com.example.ordermonitor.stockexch.ExchConfig;

import java.util.List;

public interface ExchClient {

    void setConfig(ExchConfig config);

    ExchConfig getConfig();

    List<SEOrderWrapper> requestOrderList();
    SEOrderWrapper requestOrderDetails(String instId, String orderId);

}
