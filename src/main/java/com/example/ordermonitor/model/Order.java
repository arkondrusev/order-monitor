package com.example.ordermonitor.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "se_order")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="se_order_id_seq")
    @SequenceGenerator(name="se_order_id_seq", sequenceName="se_order_id_seq", allocationSize=1)
    @Column(name = "id", updatable=false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "se_api_account_id")
    private ApiAccount apiAccount;
    @Column(name = "se_order_id")
    private String seOrderId;
    private String type;
    private String instrument;
    @Column(name = "trade_side")
    private String tradeSide;
    private BigDecimal quantity;
    private BigDecimal price;
    @Column(name = "set_timestamp")
    private ZonedDateTime openTimestamp;
    @Column(name = "execute_timestamp")
    private ZonedDateTime executeTimestamp;
    private String state;

    public Order() {
    }

    public Order(Long id, ApiAccount apiAccount, String seOrderId, String type,
                 String instrument, String tradeSide, BigDecimal quantity, BigDecimal price,
                 ZonedDateTime openTimestamp, ZonedDateTime executeTimestamp, String state) {
        this.id = id;
        this.apiAccount = apiAccount;
        this.seOrderId = seOrderId;
        this.type = type;
        this.instrument = instrument;
        this.tradeSide = tradeSide;
        this.quantity = quantity;
        this.price = price;
        this.openTimestamp = openTimestamp;
        this.executeTimestamp = executeTimestamp;
        this.state = state;
    }

}
