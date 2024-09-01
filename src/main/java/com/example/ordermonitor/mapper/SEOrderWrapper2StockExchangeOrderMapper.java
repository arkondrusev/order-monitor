package com.example.ordermonitor.mapper;

import com.example.ordermonitor.dto.OrderWrapper;
import com.example.ordermonitor.model.ApiAccount;
import com.example.ordermonitor.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(config = OrderMonitorMapperConfig.class)
public abstract class SEOrderWrapper2StockExchangeOrderMapper {

    public static SEOrderWrapper2StockExchangeOrderMapper INSTANCE = Mappers
            .getMapper(SEOrderWrapper2StockExchangeOrderMapper.class);

    public Order SEOrderWrapper2StockExchangeOrder(OrderWrapper wrapper,
                                                   ApiAccount apiAccount) {
        Order stockExchangeOrder = new Order();
        stockExchangeOrder.setId(null);
        stockExchangeOrder.setStockExchangeApiAccount(apiAccount);
        stockExchangeOrder.setSeOrderId(wrapper.getOrderId());
        stockExchangeOrder.setType(wrapper.getOrderType());
        stockExchangeOrder.setInstrument(wrapper.getInstrument());
        stockExchangeOrder.setTradeSide(wrapper.getTradeSide());
        stockExchangeOrder.setQuantity(new BigDecimal(wrapper.getQuantity()));
        stockExchangeOrder.setPrice(new BigDecimal(wrapper.getPrice()));
        stockExchangeOrder.setOpenTimestamp(calcZonedDateTime(wrapper.getOpenTimestamp()));
        stockExchangeOrder.setExecuteTimestamp(calcZonedDateTime(wrapper.getCloseTimestamp()));
        stockExchangeOrder.setState(wrapper.getState());
        return stockExchangeOrder;
    }

    private static ZonedDateTime calcZonedDateTime(String epochSecondsString) {
        Long openTimestamp = Long.parseLong(epochSecondsString);
        Instant instantTimestamp = Instant.ofEpochMilli(openTimestamp);
        return ZonedDateTime.ofInstant(instantTimestamp, ZoneId.of("UTC"));
    }

}
