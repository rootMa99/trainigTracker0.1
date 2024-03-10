package com.aptiv.trainig_tracker.controllers;

import com.aptiv.trainig_tracker.models.OrderDto;
import com.aptiv.trainig_tracker.models.OrderRest;
import com.aptiv.trainig_tracker.models.StatusRest;
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
    public OrderRest getOrderByID(@RequestParam String orderId){
        return otherService.getOderById(orderId);
    }
    @GetMapping
    public List<Long> getMatriculesBySLName(@RequestParam String shiftLeader){
        return  employeeService.getMatriculesBySl(shiftLeader);
    }
    @GetMapping(path = "/orders")
    public List<OrderRest> getAllOrderBySl(@RequestParam String shiftLeader){
        return otherService.getAllOrderBySl(shiftLeader);
    }
    @GetMapping(path = "/orders/dateBetween")
    public  List<OrderRest> getOrdersByDateBetween(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate){
        return otherService.getOrdersByDateBetween(startDate, endDate);
    }

    @PutMapping(path = "/EditOrder")
    public StatusRest EditOrder(@RequestParam String orderId, @RequestBody OrderDto orderDto){
        return otherService.updateOrder(orderId, orderDto);
    }
}
