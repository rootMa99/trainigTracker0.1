package com.aptiv.trainig_tracker.services;

import com.aptiv.trainig_tracker.models.*;

import java.util.Date;
import java.util.List;

public interface OtherService {
    StatusRest saveOrderToDb(List<OrderDto> orderDtoList);

    OrderRest getOderById(String orderId);

    List<OrderRest> getAllOrderBySl(String slName,Date startDate, Date endDate);

    List<OrderRest> getOrdersByDateBetween(Date startDate, Date endDate);

    StatusRest updateOrder(String orderId, OrderDto o);

    void deleteOrder(String orderID);

    void deleteMultiOrder(List<String> orderIds);

    List<TrainingTypeAndTitlesDto> getAllTrainingsTypeAndTitles();

    HandyData getCategoriesAndDepartments();

    List<String> getShiftLeaders();

    List<UserRest> getUserRest();

    void changePwd(String name, String pwd);

    void editDateByTrainer(Date updatedDate, List<String> orderTds);
}
