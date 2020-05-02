package com.gmail.ezlotnikova.web.controller.web;

import java.math.BigDecimal;

import com.gmail.ezlotnikova.service.OrderService;
import com.gmail.ezlotnikova.service.model.ShowOrderDTO;
import com.gmail.ezlotnikova.service.model.UserDTO;
import com.gmail.ezlotnikova.service.security.AppUser;
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
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.SUCCESS_MESSAGE;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerAddOrderTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_returnRedirectAndView() throws Exception {
        ShowOrderDTO order = getShowOrderDTO();
        AppUser appUser = getAppUser();
        Long itemId = Long.parseLong(VALID_ID);
        Long userId = Long.parseLong(VALID_ID);
        when(orderService.saveNewOrder(userId, itemId, 1)).thenReturn(order);
        mockMvc.perform(
                post("/orders")
                        .with(user(appUser))
                        .param("itemId", VALID_ID)
                        .param("amount", "1")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenInvalidRequest_returnBadRequest() throws Exception {
        mockMvc.perform(
                post("/orders")
                        .param("itemId", INVALID_ID)
                        .param("amount", "1")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_callBusinessLogic() throws Exception {
        ShowOrderDTO order = getShowOrderDTO();
        AppUser appUser = getAppUser();
        Long itemId = Long.parseLong(VALID_ID);
        Long userId = Long.parseLong(VALID_ID);
        when(orderService.saveNewOrder(userId, itemId, 1)).thenReturn(order);
        mockMvc.perform(
                post("/orders")
                        .with(user(appUser))
                        .param("itemId", VALID_ID)
                        .param("amount", "1")
        ).andExpect(status().is3xxRedirection());
        verify(orderService, times(1)).saveNewOrder(userId, itemId, 1);
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_showDuplicatedItem() throws Exception {
        ShowOrderDTO order = getShowOrderDTO();
        AppUser appUser = getAppUser();
        Long itemId = Long.parseLong(VALID_ID);
        Long userId = Long.parseLong(VALID_ID);
        when(orderService.saveNewOrder(userId, itemId, 1)).thenReturn(order);
        mockMvc.perform(
                post("/orders")
                        .with(user(appUser))
                        .param("itemId", VALID_ID)
                        .param("amount", "1")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"))
                .andExpect(flash().attribute(
                        SUCCESS_MESSAGE, "New order was successfully processed"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenInvalidRequest_returnErrorView() throws Exception {
        mockMvc.perform(
                post("/orders")
                        .param("itemId", INVALID_ID)
                        .param("amount", "1")
        ).andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString(ARGUMENT_TYPE_MISMATCH_MESSAGE)));
    }

    private AppUser getAppUser() {
        UserDTO user = new UserDTO();
        user.setId(1L);
        return new AppUser(user);
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