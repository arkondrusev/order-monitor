package com.example.ordermonitor.mapper;

import com.example.ordermonitor.dto.SEOrderWrapper;
import com.example.ordermonitor.model.StockExchange;
import com.example.ordermonitor.model.StockExchangeOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(config = OrderMonitorMapperConfig.class)
public interface SEOrderWrapper2StockExchangeOrderMapper {

    SEOrderWrapper2StockExchangeOrderMapper INSTANCE = Mappers.getMapper(SEOrderWrapper2StockExchangeOrderMapper.class);

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "stockExchange", source = "stockExchange")
    @Mapping(target = "seOrderId", source = "orderId")
    @Mapping(target = "type", source = "orderType")
    @Mapping(target = "instrument", source = "instrument")
    @Mapping(target = "tradeSide", source = "tradeSide")
    @Mapping(target = "quantity", source = "java(new BigDecimal(orderWrapper.getQuantity()))")
    @Mapping(target = "price", source = "java(new BigDecimal(orderWrapper.getPrice()))")
    @Mapping(target = "openTimestamp", source = "java(calcZonedDateTime(openTimestamp))")
    @Mapping(target = "executeTimestamp", source = "java(null)")
    @Mapping(target = "state", source = "state")
    StockExchangeOrder SEOrderWrapper2StockExchangeOrder(SEOrderWrapper request, StockExchange stockExchange);

    private ZonedDateTime calcZonedDateTime(String epochSecondsString) {
        Long openTimestamp = Long.parseLong(epochSecondsString);
        Instant instantTimestamp = Instant.ofEpochSecond(openTimestamp);
        return ZonedDateTime.ofInstant(instantTimestamp, ZoneId.of("UTC"));
    }

}
