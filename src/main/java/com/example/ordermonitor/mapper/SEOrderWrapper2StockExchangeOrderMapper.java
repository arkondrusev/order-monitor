package com.example.ordermonitor.mapper;

import com.example.ordermonitor.dto.SEOrderWrapper;
import com.example.ordermonitor.model.StockExchangeOrder;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

public interface SEOrderWrapper2StockExchangeOrderMapper {

    SEOrderWrapper2StockExchangeOrderMapper INSTANCE = Mappers.getMapper(SEOrderWrapper2StockExchangeOrderMapper.class);

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "stockExchange", source = "tagName")
    @Mapping(target = "seOrderId", source = "orderId")
    @Mapping(target = "type", source = "orderType")
    @Mapping(target = "instrument", source = "instrument")
    @Mapping(target = "tradeSide", source = "tradeSide")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "openTimestamp", source = "openTimestamp")
    @Mapping(target = "executeTimestamp", source = "java(null)")
    @Mapping(target = "state", source = "state")
    StockExchangeOrder SEOrderWrapper2StockExchangeOrder(SEOrderWrapper request);

}
