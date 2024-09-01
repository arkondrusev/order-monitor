package com.example.ordermonitor.repository;

import com.example.ordermonitor.model.StockExchange;
import com.example.ordermonitor.model.ApiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiAccountRepository extends JpaRepository<ApiAccount, Long> {

    List<ApiAccount> findAllByStockExchange(StockExchange stockExchange);

}
