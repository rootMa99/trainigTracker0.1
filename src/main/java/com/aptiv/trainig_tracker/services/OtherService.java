package com.aptiv.trainig_tracker.services;

import com.aptiv.trainig_tracker.models.OrderDto;
import com.aptiv.trainig_tracker.models.OrderRest;
import com.aptiv.trainig_tracker.models.StatusRest;

import java.util.Date;
import java.util.List;

public interface OtherService {
    StatusRest saveOrderToDb(List<OrderDto> orderDtoList);

    OrderRest getOderById(String orderId);

    List<OrderRest> getAllOrderBySl(String slName);

    List<OrderRest> getOrdersByDateBetween(Date startDate, Date endDate);

    StatusRest updateOrder(String orderId, OrderDto o);

    void deleteOrder(String orderID);

    void deleteMultiOrder(List<String> orderIds);
}
