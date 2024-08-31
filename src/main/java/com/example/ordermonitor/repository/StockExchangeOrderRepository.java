package com.example.ordermonitor.repository;

import com.example.ordermonitor.model.StockExchangeOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockExchangeOrderRepository extends CrudRepository<StockExchangeOrder, Long> {

    List<StockExchangeOrder> findAllByState(String state);

}
