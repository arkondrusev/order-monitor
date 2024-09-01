package com.example.ordermonitor.mapper;

import com.example.ordermonitor.dto.order.ExchOrderWrapper;
import com.example.ordermonitor.model.ApiAccount;
import com.example.ordermonitor.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(config = OrderMonitorMapperConfig.class)
public abstract class ExchOrderWrapper2OrderMapper {

    public static ExchOrderWrapper2OrderMapper INSTANCE = Mappers
            .getMapper(ExchOrderWrapper2OrderMapper.class);

    public Order OrderWrapper2Order(ExchOrderWrapper wrapper,
                                    ApiAccount apiAccount) {
        Order order = new Order();
        order.setId(null);
        order.setApiAccount(apiAccount);
        order.setSeOrderId(wrapper.getOrderId());
        order.setType(wrapper.getOrderType());
        order.setInstrument(wrapper.getInstrument());
        order.setTradeSide(wrapper.getTradeSide());
        order.setQuantity(new BigDecimal(wrapper.getQuantity()));
        order.setPrice(new BigDecimal(wrapper.getPrice()));
        order.setOpenTimestamp(calcZonedDateTime(wrapper.getOpenTimestamp()));
        order.setExecuteTimestamp(calcZonedDateTime(wrapper.getCloseTimestamp()));
        order.setState(wrapper.getState());
        return order;
    }

    private static ZonedDateTime calcZonedDateTime(String epochSecondsString) {
        Long openTimestamp = Long.parseLong(epochSecondsString);
        Instant instantTimestamp = Instant.ofEpochMilli(openTimestamp);
        return ZonedDateTime.ofInstant(instantTimestamp, ZoneId.of("UTC"));
    }

}
