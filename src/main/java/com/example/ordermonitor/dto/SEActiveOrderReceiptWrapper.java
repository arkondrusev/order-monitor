package com.example.ordermonitor.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SEActiveOrderReceiptWrapper {

    private final String orderId;
    private final String orderType;
    private final String instrument;
    private final String tradeSide;
    private final String quantity;
    private final String price;
    private final String setTimestamp;

    @JsonCreator
    public SEActiveOrderReceiptWrapper(
            @JsonProperty("ordId") String orderId,
            @JsonProperty("ordType") String orderType,
            @JsonProperty("instId") String instrument,
            @JsonProperty("side") String tradeSide,
            @JsonProperty("sz") String quantity,
            @JsonProperty("px") String price,
            @JsonProperty("cTime") String setTimestamp
    ) {
        this.orderId = orderId;
        this.orderType = orderType;
        this.instrument = instrument;
        this.tradeSide = tradeSide;
        this.quantity = quantity;
        this.price = price;
        this.setTimestamp = setTimestamp;
    }

}

