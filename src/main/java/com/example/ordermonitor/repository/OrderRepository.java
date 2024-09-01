package com.example.ordermonitor.repository;

import com.example.ordermonitor.model.ApiAccount;
import com.example.ordermonitor.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByState(String state);

    List<Order> findAllByApiAccount(ApiAccount apiAccount);

}
