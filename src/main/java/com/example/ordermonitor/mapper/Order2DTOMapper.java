package com.example.ordermonitor.mapper;

import com.example.ordermonitor.dto.order.OrderRestWrapper;
import com.example.ordermonitor.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = OrderMonitorMapperConfig.class)
public interface Order2DTOMapper {

    Order2DTOMapper INSTANCE = Mappers
            .getMapper(Order2DTOMapper.class);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "apiAccountId", source = "order.apiAccount.id")
    @Mapping(target = "orderType", source = "type")
    @Mapping(target = "instrument", source = "instrument")
    @Mapping(target = "tradeSide", source = "tradeSide")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "openTimestamp", source = "order.openTimestamp")
    @Mapping(target = "closeTimestamp", source = "order.executeTimestamp")
    @Mapping(target = "state", source = "order.state")
    OrderRestWrapper order2RestOrderWrapper(Order order);

    List<OrderRestWrapper> order2RestOrderWrapper(List<Order> order);

}
