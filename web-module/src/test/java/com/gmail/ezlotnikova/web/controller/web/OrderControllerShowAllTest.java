package com.gmail.ezlotnikova.web.controller.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.gmail.ezlotnikova.service.ItemService;
import com.gmail.ezlotnikova.service.OrderService;
import com.gmail.ezlotnikova.service.model.ItemPreviewDTO;
import com.gmail.ezlotnikova.service.model.OrderPreviewDTO;
import com.gmail.ezlotnikova.web.controller.web.OrderController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.INVALID_PAGE_NUMBER;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ITEM_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ITEM_UNIQUE_NUMBER;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.OBJECTS_BY_PAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ORDER_STATUS;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_PAGE_NUMBER;
import static com.gmail.ezlotnikova.web.controller.constant.ExceptionHandlerConstant.ARGUMENT_TYPE_MISMATCH_MESSAGE;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = OrderController.class)
public class OrderControllerShowAllTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_returnView() throws Exception {
        Page<OrderPreviewDTO> orders = getPageOfOrders();
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        when(orderService.findPaginatedAndOrderedByDate(pageNumber)).thenReturn(orders);
        mockMvc.perform(
                get("/orders")
                        .param("page", VALID_PAGE_NUMBER)
        ).andExpect(status().isOk())
                .andExpect(view().name("order_previews"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenInvalidRequest_returnBadRequest() throws Exception {
        mockMvc.perform(
                get("/orders")
                        .param("page", INVALID_PAGE_NUMBER)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_callBusinessLogic() throws Exception {
        Page<OrderPreviewDTO> orders = getPageOfOrders();
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        when(orderService.findPaginatedAndOrderedByDate(pageNumber)).thenReturn(orders);
        mockMvc.perform(
                get("/orders")
                        .param("page", VALID_PAGE_NUMBER)
        ).andExpect(status().isOk());
        verify(orderService, times(1)).findPaginatedAndOrderedByDate(pageNumber);
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_returnViewWithOrders() throws Exception {
        Page<OrderPreviewDTO> orders = getPageOfOrders();
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        when(orderService.findPaginatedAndOrderedByDate(pageNumber)).thenReturn(orders);
        mockMvc.perform(
                get("/orders")
                        .param("page", VALID_PAGE_NUMBER)
        ).andExpect(status().isOk())
                .andExpect(content().string(containsString(ORDER_STATUS.name())))
                .andExpect(content().string(containsString(ITEM_NAME)));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenInvalidRequest_returnErrorView() throws Exception {
        mockMvc.perform(
                get("/orders")
                        .param("page", INVALID_PAGE_NUMBER)
        ).andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString(ARGUMENT_TYPE_MISMATCH_MESSAGE)));
    }

    private Page<OrderPreviewDTO> getPageOfOrders() {
        OrderPreviewDTO order = getOrderPreviewDTO();
        List<OrderPreviewDTO> orderList = new ArrayList<>();
        orderList.add(order);
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, OBJECTS_BY_PAGE);
        return new PageImpl<>(orderList, pageRequest, 1);
    }

    private OrderPreviewDTO getOrderPreviewDTO() {
        OrderPreviewDTO order = new OrderPreviewDTO();
        order.setId(1L);
        order.setStatus(ORDER_STATUS);
        order.setItemName(ITEM_NAME);
        order.setAmount(1);
        order.setSum(BigDecimal.ONE);
        return order;
    }

}