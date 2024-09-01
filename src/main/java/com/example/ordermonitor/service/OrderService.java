package com.example.ordermonitor.service;

import com.example.ordermonitor.dto.order.GetOrderListRequest;
import com.example.ordermonitor.dto.order.GetOrderListResponse;
import com.example.ordermonitor.model.Order;
import com.example.ordermonitor.model.ApiAccount;
import com.example.ordermonitor.repository.OrderRepository;
import com.example.ordermonitor.repository.StockExchangeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private static final String ORDER_STATE_LIVE = "live";

    public OrderService(OrderRepository orderRepository,
                        StockExchangeRepository stockExchangeRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getLiveOrderList(ApiAccount apiAccount) {
        List<Order> liveOrderList = orderRepository.findAllByApiAccount(apiAccount);
        return liveOrderList.stream().filter(e -> e.getState().equals(ORDER_STATE_LIVE)).collect(Collectors.toList());
    }

    public Order save(Order stockExchangeOrder) {
        return orderRepository.save(stockExchangeOrder);
    }

    public GetOrderListResponse getOrderList(GetOrderListRequest request) {
        return null;
    }

}
