package com.example.ordermonitor.controller;

import com.example.ordermonitor.dto.monitor.GetOrderCheckPeriodResponse;
import com.example.ordermonitor.dto.monitor.SetOrderCheckPeriodRequest;
import com.example.ordermonitor.dto.monitor.SetOrderCheckPeriodResponse;
import com.example.ordermonitor.service.MonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("monitor")
@RestController
@RequiredArgsConstructor
public class MonitorController {

    private final MonitorService monitorService;

    @PutMapping("set-period")
    public SetOrderCheckPeriodResponse setOrderCheckPeriod(@RequestBody SetOrderCheckPeriodRequest request) {
        return monitorService.setOrderCheckPeriod(request);
    }

    @GetMapping("get-period")
    public GetOrderCheckPeriodResponse getOrderCheckPeriod() {
        return monitorService.getOrderCheckPeriod();
    }

}
