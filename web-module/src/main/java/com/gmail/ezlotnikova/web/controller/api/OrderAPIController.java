package com.gmail.ezlotnikova.web.controller.api;

import com.gmail.ezlotnikova.service.OrderService;
import com.gmail.ezlotnikova.service.model.OrderPreviewDTO;
import com.gmail.ezlotnikova.service.model.ShowOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")
public class OrderAPIController {

    private final OrderService orderService;

    public OrderAPIController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Page<OrderPreviewDTO> showAllOrders(
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        return orderService.findPaginatedAndOrderedByDate(page);
    }

    @GetMapping("/{id}")
    public ShowOrderDTO showOrderById(
            @PathVariable(name = "id") Long id
    ) {
        return orderService.findById(id);
    }

}