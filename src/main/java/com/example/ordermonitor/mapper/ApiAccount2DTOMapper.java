package com.example.ordermonitor.mapper;

import com.example.ordermonitor.dto.account.*;
import com.example.ordermonitor.model.ApiAccount;
import com.example.ordermonitor.model.StockExchange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = OrderMonitorMapperConfig.class)
public interface ApiAccount2DTOMapper {

    ApiAccount2DTOMapper INSTANCE = Mappers.getMapper(ApiAccount2DTOMapper.class);

    @Mapping(target = "accountId", source = "apiAccount.id")
    @Mapping(target = "accountName", source = "apiAccount.name")
    @Mapping(target = "stockExchangeId", source = "apiAccount.stockExchange.name")
    @Mapping(target = "telegramUsername", source = "apiAccount.telegramUsername")
    @Mapping(target = "responseCode", source = "responseCode")
    @Mapping(target = "responseMessage", source = "responseMessage")
    CreateApiAccountResponse apiAccount2CreateApiAccountResponse(ApiAccount apiAccount,
                                                                 Integer responseCode,
                                                                 String responseMessage);

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "name", source = "request.accountName")
    @Mapping(target = "stockExchange", source = "stockExchange")
    @Mapping(target = "telegramUsername", source = "request.telegramUsername")
    @Mapping(target = "exchClient", expression = "java(null)")
    ApiAccount createApiAccountRequest2ApiAccount(CreateApiAccountRequest request, StockExchange stockExchange);

    @Mapping(target = "account", source = "apiAccountWrapper")
    @Mapping(target = "responseCode", source = "responseCode")
    @Mapping(target = "responseMessage", source = "responseMessage")
    UpdateApiAccountResponse apiAccount2UpdateApiAccountResponse(ApiAccountRestWrapper apiAccountWrapper,
                                                                 Integer responseCode,
                                                                 String responseMessage);

    @Mapping(target = "accountId", source = "apiAccount.id")
    @Mapping(target = "accountName", source = "apiAccount.name")
    @Mapping(target = "stockExchangeId", source = "apiAccount.stockExchange.id")
    @Mapping(target = "telegramUsername", source = "apiAccount.telegramUsername")
    ApiAccountRestWrapper apiAccount2ApiAccountRestWrapper(ApiAccount apiAccount);

    List<ApiAccountRestWrapper> apiAccount2ApiAccountRestWrapper(List<ApiAccount> apiAccount);

}
