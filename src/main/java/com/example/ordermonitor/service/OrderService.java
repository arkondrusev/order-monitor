package com.example.ordermonitor.service;

import com.example.ordermonitor.dto.order.GetOrderListRequest;
import com.example.ordermonitor.dto.order.GetOrderListResponse;
import com.example.ordermonitor.mapper.Order2RestOrderWrapperMapper;
import com.example.ordermonitor.model.ApiAccount;
import com.example.ordermonitor.model.Order;
import com.example.ordermonitor.repository.ApiAccountRepository;
import com.example.ordermonitor.repository.OrderRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IRestService {

    private final OrderRepository orderRepository;
    private final ApiAccountRepository apiAccountRepository;

    private static final String ORDER_STATE_LIVE = "live";

    public List<Order> getLiveOrderList(ApiAccount apiAccount) {
        List<Order> liveOrderList = orderRepository.findAllByApiAccount(apiAccount);
        return liveOrderList.stream().filter(e -> e.getState().equals(ORDER_STATE_LIVE)).collect(Collectors.toList());
    }

    public Order save(Order stockExchangeOrder) {
        return orderRepository.save(stockExchangeOrder);
    }

    public GetOrderListResponse getOrderList(GetOrderListRequest request) {
        try {
            checkGetOrderListRequestParams(request);
            Optional<ApiAccount> apiAccountOpt = apiAccountRepository.findById(request.getApiAccountId());
            if (apiAccountOpt.isEmpty()) {
                throw new IllegalArgumentException("Api account not found");
            }
            //todo implement other filters
            List<Order> orderList = orderRepository.findAllByApiAccount(apiAccountOpt.get());
            return new GetOrderListResponse(RESPONSE_CODE_OK, RESPONSE_MESSAGE_OK,
                    Order2RestOrderWrapperMapper.INSTANCE.order2RestOrderWrapper(orderList));
        } catch (Exception e) {
            return new GetOrderListResponse(RESPONSE_CODE_ERROR, e.getMessage(), null);
        }
    }

    private void checkGetOrderListRequestParams(@NonNull GetOrderListRequest request) {
        if (request.getApiAccountId() == null) {
            throw new IllegalArgumentException("Api account id is required");
        }
    }

}
