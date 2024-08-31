package com.example.ordermonitor.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "se_api_account")
@Data
@NoArgsConstructor
public class StockExchangeApiAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="se_api_account_id_seq")
    @SequenceGenerator(name="se_api_account_id_seq", sequenceName="se_api_account_id_seq", allocationSize=1)
    @Column(name = "id", updatable=false)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "se_id")
    private StockExchange stockExchange;

}
