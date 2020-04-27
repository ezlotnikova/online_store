package com.gmail.ezlotnikova.service.impl;

import javax.transaction.Transactional;

import com.gmail.ezlotnikova.repository.OrderRepository;
import com.gmail.ezlotnikova.repository.model.Order;
import com.gmail.ezlotnikova.repository.model.сonstant.OrderStatusEnum;
import com.gmail.ezlotnikova.service.OrderService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.OrderPreviewDTO;
import com.gmail.ezlotnikova.service.model.ShowOrderDTO;
import com.gmail.ezlotnikova.service.util.converter.OrderConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.NO_OBJECT_FOUND;
import static com.gmail.ezlotnikova.service.constant.PaginationConstant.ORDERS_BY_PAGE;
import static com.gmail.ezlotnikova.service.util.converter.OrderConverter.convertToShowOrderDTO;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public Page<OrderPreviewDTO> findPaginatedAndOrderedByDate(int pageNumber) {
         /* page numeration in UI starts from 1, but in Pageable and Page objects it starts from zero,
        so parameter passed to PageRequest constructor is "pageNumber - 1" */
        Pageable pageRequest = PageRequest.of(pageNumber - 1, ORDERS_BY_PAGE);
        return orderRepository.findPaginatedAndOrderedByDate(pageRequest)
                .map(OrderConverter::convertToOrderPreviewDTO);
    }

    @Override
    @Transactional
    @Nullable
    public ShowOrderDTO findById(Long id) {
        Order order = orderRepository.findById(id);
        if (order != null) {
            return convertToShowOrderDTO(order);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public ExecutionResult updateOrderStatusById(Long id, OrderStatusEnum newStatus) {
        Order order = orderRepository.findById(id);
        if (order != null) {
            order.setStatus(newStatus);
            orderRepository.merge(order);
            return ExecutionResult.ok();
        } else {
            return ExecutionResult.error(NO_OBJECT_FOUND, "No order with id " + id + " found. ");
        }
    }

}