package com.aptiv.trainig_tracker.services;

import com.aptiv.trainig_tracker.models.OrderDto;
import com.aptiv.trainig_tracker.models.StatusRest;

import java.util.List;

public interface OtherService {
    StatusRest saveOrderToDb(List<OrderDto> orderDtoList);
}
