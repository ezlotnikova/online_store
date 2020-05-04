package com.gmail.ezlotnikova.web.controller.unit.web;

import java.math.BigDecimal;

import com.gmail.ezlotnikova.service.OrderService;
import com.gmail.ezlotnikova.service.model.ShowOrderDTO;
import com.gmail.ezlotnikova.web.controller.web.OrderController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.INVALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ITEM_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ORDER_STATUS;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_TELEPHONE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ID;
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
public class OrderControllerShowByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_returnView() throws Exception {
        ShowOrderDTO order = getShowOrderDTO();
        Long id = Long.parseLong(VALID_ID);
        when(orderService.findById(id)).thenReturn(order);
        mockMvc.perform(
                get("/orders/" + VALID_ID)
        ).andExpect(status().isOk())
                .andExpect(view().name("order_details"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenInvalidRequest_returnErrorView() throws Exception {
        mockMvc.perform(
                get("/orders/" + INVALID_ID)
        ).andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString(ARGUMENT_TYPE_MISMATCH_MESSAGE)));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_callBusinessLogic() throws Exception {
        ShowOrderDTO order = getShowOrderDTO();
        Long id = Long.parseLong(VALID_ID);
        when(orderService.findById(id)).thenReturn(order);
        mockMvc.perform(
                get("/orders/" + VALID_ID)
        ).andExpect(status().isOk());
        verify(orderService, times(1)).findById(id);
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_returnOrder() throws Exception {
        ShowOrderDTO order = getShowOrderDTO();
        Long id = Long.parseLong(VALID_ID);
        when(orderService.findById(id)).thenReturn(order);
        mockMvc.perform(
                get("/orders/" + VALID_ID)
        ).andExpect(status().isOk())
                .andExpect(content().string(containsString(ORDER_STATUS.name())))
                .andExpect(content().string(containsString(ITEM_NAME)));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenIDDoesNotExist_returnErrorView() throws Exception {
        mockMvc.perform(
                get("/orders/" + VALID_ID)
        ).andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString("Order not found")));
    }

    private ShowOrderDTO getShowOrderDTO() {
        ShowOrderDTO order = new ShowOrderDTO();
        order.setId(Long.parseLong(VALID_ID));
        order.setStatus(ORDER_STATUS);
        order.setUserId(Long.parseLong(VALID_ID));
        order.setUserTelephone(USER_TELEPHONE);
        order.setItemName(ITEM_NAME);
        order.setAmount(1);
        order.setSum(BigDecimal.ONE);
        return order;
    }

}