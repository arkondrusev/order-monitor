package com.example.ordermonitor.mapper;

import com.example.ordermonitor.dto.stockexchange.*;
import com.example.ordermonitor.model.StockExchange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = OrderMonitorMapperConfig.class)
public interface StockExchange2DTOMapper {

    StockExchange2DTOMapper INSTANCE = Mappers.getMapper(StockExchange2DTOMapper.class);

    @Mapping(target = "id", source = "stockExchange.id")
    @Mapping(target = "name", source = "stockExchange.name")
    @Mapping(target = "responseCode", source = "responseCode")
    @Mapping(target = "responseMessage", source = "responseMessage")
    CreateStockExchangeResponse stockExchange2CreateStockExchangeResponse(StockExchange stockExchange,
                                                                          Integer responseCode,
                                                                          String responseMessage);

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "name", source = "name")
    StockExchange createStockExchangeRequest2StockExchange(CreateStockExchangeRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    StockExchange updateStockExchangeRequest2StockExchange(UpdateStockExchangeRequest request);

    @Mapping(target = "id", source = "stockExchange.id")
    @Mapping(target = "name", source = "stockExchange.name")
    @Mapping(target = "responseCode", source = "responseCode")
    @Mapping(target = "responseMessage", source = "responseMessage")
    UpdateStockExchangeResponse stockExchange2UpdateStockExchangeResponse(StockExchange stockExchange,
                                                                          Integer responseCode,
                                                                          String responseMessage);

    @Mapping(target = "id", source = "stockExchange.id")
    @Mapping(target = "name", source = "stockExchange.name")
    StockExchangeRestWrapper stockExchange2StockExchangeRestWrapper(StockExchange stockExchange);

    List<StockExchangeRestWrapper> stockExchange2StockExchangeRestWrapper(List<StockExchange> stockExchanges);

}
