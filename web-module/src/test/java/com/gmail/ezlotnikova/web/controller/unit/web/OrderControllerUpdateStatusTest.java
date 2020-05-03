package com.gmail.ezlotnikova.web.controller.unit.web;

import com.gmail.ezlotnikova.service.OrderService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.web.controller.web.OrderController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.NO_OBJECT_FOUND;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.INVALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ORDER_STATUS;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ExceptionHandlerConstant.ARGUMENT_TYPE_MISMATCH_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.FAILURE_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.SUCCESS_MESSAGE;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = OrderController.class)
public class OrderControllerUpdateStatusTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_returnRedirect() throws Exception {
        ExecutionResult result = ExecutionResult.ok();
        Long id = Long.parseLong(VALID_ID);
        when(orderService.updateOrderStatusById(id, ORDER_STATUS)).thenReturn(result);
        mockMvc.perform(
                post("/orders/" + VALID_ID)
                        .param("newStatus", ORDER_STATUS.name())
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenInvalidRequest_returnBadRequest() throws Exception {
        mockMvc.perform(
                post("/orders/" + INVALID_ID)
                        .param("newStatus", ORDER_STATUS.name())
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_callBusinessLogic() throws Exception {
        ExecutionResult result = ExecutionResult.ok();
        Long id = Long.parseLong(VALID_ID);
        when(orderService.updateOrderStatusById(id, ORDER_STATUS)).thenReturn(result);
        mockMvc.perform(
                post("/orders/" + VALID_ID)
                        .param("newStatus", ORDER_STATUS.name())
        ).andExpect(status().is3xxRedirection());
        verify(orderService, times(1)).updateOrderStatusById(id, ORDER_STATUS);
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_returnSuccessMessage() throws Exception {
        ExecutionResult result = ExecutionResult.ok();
        Long id = Long.parseLong(VALID_ID);
        when(orderService.updateOrderStatusById(id, ORDER_STATUS)).thenReturn(result);
        mockMvc.perform(
                post("/orders/" + VALID_ID)
                        .param("newStatus", ORDER_STATUS.name())
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"))
                .andExpect(flash().attribute(
                        SUCCESS_MESSAGE, "Status of order #" + id + " successfully changed to " + ORDER_STATUS.name()));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenIdDoesNotExist_returnFailureMessage() throws Exception {
        Long id = Long.parseLong(VALID_ID);
        ExecutionResult result = ExecutionResult.error(NO_OBJECT_FOUND, "No order with id " + id + " found. ");
        when(orderService.updateOrderStatusById(id, ORDER_STATUS)).thenReturn(result);
        mockMvc.perform(
                post("/orders/" + VALID_ID)
                        .param("newStatus", ORDER_STATUS.name())
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"))
                .andExpect(flash().attribute(FAILURE_MESSAGE, result.getErrorMessage()));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenInvalidRequest_returnErrorView() throws Exception {
        mockMvc.perform(
                post("/orders/" + INVALID_ID)
                        .param("newStatus", ORDER_STATUS.name())
        ).andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString(ARGUMENT_TYPE_MISMATCH_MESSAGE)));
    }

}