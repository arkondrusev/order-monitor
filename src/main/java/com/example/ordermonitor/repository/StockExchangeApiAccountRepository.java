package com.example.ordermonitor.repository;

import com.example.ordermonitor.model.StockExchange;
import com.example.ordermonitor.model.StockExchangeApiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockExchangeApiAccountRepository extends JpaRepository<StockExchangeApiAccount, Long> {

    List<StockExchangeApiAccount> findAllByStockExchange(StockExchange stockExchange);

}
