package com.example.ordermonitor.stockexch.client;

import com.example.ordermonitor.dto.SEOrderWrapper;
import com.example.ordermonitor.stockexch.ExchConfig;
import org.springframework.web.client.RestClient;

import java.util.List;

public abstract class AbstractExchClient implements ExchClient {

    protected RestClient restClient;
    protected ExchConfig config;

    public abstract List<SEOrderWrapper> requestOrderList();
    public abstract SEOrderWrapper requestOrderDetails(String intrId, String orderId);

}
