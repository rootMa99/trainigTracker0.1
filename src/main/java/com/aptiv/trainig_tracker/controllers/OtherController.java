package com.aptiv.trainig_tracker.controllers;

import com.aptiv.trainig_tracker.models.OrderDto;
import com.aptiv.trainig_tracker.models.StatusRest;
import com.aptiv.trainig_tracker.services.OtherService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/other")
@AllArgsConstructor
public class OtherController {
    OtherService otherService;

    @PostMapping(path = "/addOrder")
    public StatusRest addTrainingToEmployees(@RequestBody List<OrderDto> orderDtoList) {
        return otherService.saveOrderToDb(orderDtoList);
    }
}
