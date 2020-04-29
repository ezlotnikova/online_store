package com.gmail.ezlotnikova.service.impl;

import java.math.BigDecimal;
import javax.transaction.Transactional;

import com.gmail.ezlotnikova.repository.ItemRepository;
import com.gmail.ezlotnikova.repository.OrderRepository;
import com.gmail.ezlotnikova.repository.UserRepository;
import com.gmail.ezlotnikova.repository.model.Item;
import com.gmail.ezlotnikova.repository.model.Order;
import com.gmail.ezlotnikova.repository.model.сonstant.OrderStatusEnum;
import com.gmail.ezlotnikova.repository.model.сonstant.UserRoleEnum;
import com.gmail.ezlotnikova.service.OrderService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.OrderPreviewDTO;
import com.gmail.ezlotnikova.service.model.ShowOrderDTO;
import com.gmail.ezlotnikova.service.security.AppUser;
import com.gmail.ezlotnikova.service.util.converter.OrderConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.NO_OBJECT_FOUND;
import static com.gmail.ezlotnikova.service.constant.PaginationConstant.ORDERS_BY_PAGE;
import static com.gmail.ezlotnikova.service.util.converter.OrderConverter.convertToShowOrderDTO;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public ShowOrderDTO saveNewOrder(Long userId, Long itemId, Integer amount) {
        Order order = getNewOrder(userId, itemId, amount);
        Order addedOrder = orderRepository.persist(order);
        return convertToShowOrderDTO(addedOrder);
    }

    @Override
    @Transactional
    public Page<OrderPreviewDTO> findPaginatedAndOrderedByDate(int pageNumber) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = (AppUser) authentication.getPrincipal();
        if (isSaleUser(appUser)) {
            return findAllOrders(pageNumber);
        } else if (isCustomerUser(appUser)) {
            return findOrdersByUserId(pageNumber, appUser.getId());
        }
        return Page.empty();
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

    private Order getNewOrder(Long userId, Long itemId, Integer amount) {
        Order order = new Order();
        order.setUserDetails(userRepository
                .findById(userId)
                .getUserDetails());
        Item item = itemRepository.findById(itemId);
        order.setItem(item);
        order.setAmount(amount);
        BigDecimal sum = item.getPrice().multiply(new BigDecimal(amount));
        order.setSum(sum);
        order.setStatus(OrderStatusEnum.NEW);
        return order;
    }

    private boolean isSaleUser(AppUser appUser) {
        return appUser.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_" + UserRoleEnum.SALE_USER.name()));
    }

    private boolean isCustomerUser(AppUser appUser) {
        return appUser.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
                        .equals("ROLE_" + UserRoleEnum.CUSTOMER_USER.name()));
    }

    private Page<OrderPreviewDTO> findAllOrders(int pageNumber) {
        /* page numeration in UI starts from 1, but in Pageable and Page objects it starts from zero,
        so parameter passed to PageRequest constructor is "pageNumber - 1" */
        Pageable pageRequest = PageRequest.of(pageNumber - 1, ORDERS_BY_PAGE);
        return orderRepository.findPaginatedAndOrderedByDate(pageRequest)
                .map(OrderConverter::convertToOrderPreviewDTO);
    }

    private Page<OrderPreviewDTO> findOrdersByUserId(int pageNumber, Long id) {
        /* page numeration in UI starts from 1, but in Pageable and Page objects it starts from zero,
        so parameter passed to PageRequest constructor is "pageNumber - 1" */
        Pageable pageRequest = PageRequest.of(pageNumber - 1, ORDERS_BY_PAGE);
        return orderRepository.findPaginatedForUser(pageRequest, id)
                .map(OrderConverter::convertToOrderPreviewDTO);
    }

}