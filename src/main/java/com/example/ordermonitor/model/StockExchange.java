package com.example.ordermonitor.model;

import com.example.ordermonitor.stockexch.ExchClient;
import com.example.ordermonitor.stockexch.okx.OkxClient;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "stock_exchange")
@Data
@NoArgsConstructor
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
    @Transient
    private ExchClient exchClient;

    @PostConstruct
    private void postConstruct(OkxClient okxClient) {
        // rework later for flexible initialization
        if (id == 1) {
            exchClient = okxClient;
        }
    }

}
