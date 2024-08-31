package com.example.ordermonitor.repository;

import com.example.ordermonitor.model.StockExchangeApiAccount;
import com.example.ordermonitor.model.StockExchangeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockExchangeOrderRepository extends JpaRepository<StockExchangeOrder, Long> {

    List<StockExchangeOrder> findAllByState(String state);

    List<StockExchangeOrder> findAllByStockExchangeApiAccount(StockExchangeApiAccount stockExchangeApiAccount);

}
