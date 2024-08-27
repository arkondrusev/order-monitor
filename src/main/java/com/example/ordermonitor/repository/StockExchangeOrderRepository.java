package com.example.ordermonitor.repository;

import com.example.ordermonitor.model.StockExchangeOrder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StockExchangeOrderRepository extends CrudRepository<StockExchangeOrder, Long> {

    List<StockExchangeOrder> findByState(String state);

}
