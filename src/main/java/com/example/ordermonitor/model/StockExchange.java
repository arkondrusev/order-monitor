package com.example.ordermonitor.model;

import jakarta.persistence.*;
import lombok.NonNull;

@Entity
@Table(name = "stock_exchange")
public class StockExchange {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="stock_exchange_id_seq")
    @SequenceGenerator(name="stock_exchange_id_seq", sequenceName="stock_exchange_id_seq", allocationSize=1)
    @Column(name = "id", updatable=false)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    @Column(name = "api_url")
    private String apiUrl;
    @NonNull
    @Column(name = "order_list_endpoint")
    private String orderListEndpoint;

}
