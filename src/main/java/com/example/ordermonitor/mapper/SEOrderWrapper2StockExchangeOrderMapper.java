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
    @Mapping(target = "seOrderId", source = "wrapper.orderId")
    @Mapping(target = "type", source = "wrapper.orderType")
    @Mapping(target = "instrument", source = "wrapper.instrument")
    @Mapping(target = "tradeSide", source = "wrapper.tradeSide")
    @Mapping(target = "quantity", expression = "java(null)")
    @Mapping(target = "price", expression = "java(null)")
    @Mapping(target = "openTimestamp", expression = "java(null)")
    @Mapping(target = "executeTimestamp", expression = "java(null)")
    @Mapping(target = "state", source = "wrapper.state")
    StockExchangeOrder SEOrderWrapper2StockExchangeOrder(SEOrderWrapper wrapper, StockExchange stockExchange);

    private ZonedDateTime calcZonedDateTime(String epochSecondsString) {
        Long openTimestamp = Long.parseLong(epochSecondsString);
        Instant instantTimestamp = Instant.ofEpochSecond(openTimestamp);
        return ZonedDateTime.ofInstant(instantTimestamp, ZoneId.of("UTC"));
    }

}
