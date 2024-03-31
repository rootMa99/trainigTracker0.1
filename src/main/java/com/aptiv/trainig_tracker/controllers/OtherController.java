package com.aptiv.trainig_tracker.controllers;

import com.aptiv.trainig_tracker.models.*;
import com.aptiv.trainig_tracker.services.EmployeeService;
import com.aptiv.trainig_tracker.services.OtherService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/other")
@AllArgsConstructor
public class OtherController {
    OtherService otherService;
    EmployeeService employeeService;

    @PostMapping(path = "/addOrder")
    public StatusRest addTrainingToEmployees(@RequestBody List<OrderDto> orderDtoList) {
        return otherService.saveOrderToDb(orderDtoList);
    }

    @GetMapping(path = "/order")
    public OrderRest getOrderByID(@RequestParam String orderId) {
        return otherService.getOderById(orderId);
    }

    @GetMapping
    public List<Long> getMatriculesBySLName(@RequestParam String shiftLeader) {
        return employeeService.getMatriculesBySl(shiftLeader);
    }

    @GetMapping(path = "/orders")
    public List<OrderRest> getAllOrderBySl(@RequestParam String shiftLeader, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate) {
        return otherService.getAllOrderBySl(shiftLeader, startDate, endDate);
    }

    @GetMapping(path = "/orders/dateBetween")
    public List<OrderRest> getOrdersByDateBetween(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate) {
        return otherService.getOrdersByDateBetween(startDate, endDate);
    }

    @PutMapping(path = "/EditOrder")
    public StatusRest EditOrder(@RequestParam String orderId, @RequestBody OrderDto orderDto) {
        return otherService.updateOrder(orderId, orderDto);
    }

    @DeleteMapping(path = "/deleteOrder")
    public void deleteOrderById(@RequestParam String orderId) {
        otherService.deleteOrder(orderId);
    }

    @DeleteMapping(path = "/deleteOrders")
    public void deleteOrdersByIds(@RequestBody List<String> ordersIDs) {
        otherService.deleteMultiOrder(ordersIDs);
    }
    @GetMapping(path = "/trainingTypeAndTitle")
    public List<TrainingTypeAndTitlesDto> getTrainingsTypesAndTitles(){
        return otherService.getAllTrainingsTypeAndTitles();
    }
    @GetMapping(path = "/categoriesAndDepartments")
    public HandyData getHandyData(){
        return otherService.getCategoriesAndDepartments();
    }


    @PutMapping(path = "/updateOrder/trainer")
    public void updateOrderDateByTrainer(@RequestBody List<String> ordersIDs, @RequestParam @DateTimeFormat(pattern =
            "yyyy-MM-dd") Date endDate){
        try {
            otherService.editDateByTrainer(endDate, ordersIDs);
        }catch (Error e){
            throw new RuntimeException(e);
        }
    }
}
