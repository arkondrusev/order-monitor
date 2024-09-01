package com.example.ordermonitor.controller;

import com.example.ordermonitor.dto.order.GetOrderListRequest;
import com.example.ordermonitor.dto.order.GetOrderListResponse;
import com.example.ordermonitor.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("order")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("get-list")
    public GetOrderListResponse getOrderList(GetOrderListRequest request) {
        return orderService.getOrderList(request);
    }

}
