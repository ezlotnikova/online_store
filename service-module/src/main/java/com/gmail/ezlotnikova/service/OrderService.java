package com.gmail.ezlotnikova.service;

import com.gmail.ezlotnikova.repository.model.сonstant.OrderStatusEnum;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.OrderPreviewDTO;
import com.gmail.ezlotnikova.service.model.ShowOrderDTO;
import org.springframework.data.domain.Page;

public interface OrderService {

    ShowOrderDTO saveNewOrder(Long userId, Long itemId, Integer amount);

    Page<OrderPreviewDTO> findPaginatedAndOrderedByDate(int pageNumber);

    ShowOrderDTO findById(Long id);

    ExecutionResult updateOrderStatusById(Long id, OrderStatusEnum newStatus);

}