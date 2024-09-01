package com.example.ordermonitor.mapper;

import com.example.ordermonitor.dto.stockexchange.StockExchangeRestWrapper;
import com.example.ordermonitor.model.StockExchange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = OrderMonitorMapperConfig.class)
public interface StockExchange2StockExchangeRestWrapperMapper {

    StockExchange2StockExchangeRestWrapperMapper INSTANCE = Mappers
            .getMapper(StockExchange2StockExchangeRestWrapperMapper.class);

    @Mapping(target = "id", source = "stockExchange.id")
    @Mapping(target = "name", source = "stockExchange.name")
    StockExchangeRestWrapper stockExchange2StockExchangeRestWrapper(StockExchange stockExchange);

    List<StockExchangeRestWrapper> stockExchange2StockExchangeRestWrapper(List<StockExchange> stockExchanges);

}
