package com.example.ordermonitor.dto.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchOrderWrapper {

    private final String orderId;
    private final String orderType;
    private final String instrument;
    private final String tradeSide;
    private final String quantity;
    private final String price;
    private final String openTimestamp;
    private final String closeTimestamp;
    private final String state;

    @JsonCreator
    public ExchOrderWrapper(
            @JsonProperty("ordId") String orderId,
            @JsonProperty("ordType") String orderType,
            @JsonProperty("instId") String instrument,
            @JsonProperty("side") String tradeSide,
            @JsonProperty("sz") String quantity,
            @JsonProperty("px") String price,
            @JsonProperty("cTime") String openTimestamp,
            @JsonProperty("uTime") String closeTimestamp,
            @JsonProperty("state") String state
    ) {
        this.orderId = orderId;
        this.orderType = orderType;
        this.instrument = instrument;
        this.tradeSide = tradeSide;
        this.quantity = quantity;
        this.price = price;
        this.openTimestamp = openTimestamp;
        this.closeTimestamp = closeTimestamp;
        this.state = state;
    }

}

