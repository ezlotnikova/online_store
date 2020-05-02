package com.gmail.ezlotnikova.web.controller.web;

import com.gmail.ezlotnikova.service.ItemService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.web.controller.web.ItemController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.NO_OBJECT_FOUND;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.INVALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ExceptionHandlerConstant.ARGUMENT_TYPE_MISMATCH_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.FAILURE_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.SUCCESS_MESSAGE;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerDeleteByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private ItemService itemService;

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_returnRedirect() throws Exception {
        ExecutionResult result = ExecutionResult.ok();
        Long id = Long.parseLong(VALID_ID);
        when(itemService.deleteItemById(id)).thenReturn(result);
        mockMvc.perform(
                get("/items/" + VALID_ID + "/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenInvalidRequest_returnBadRequest() throws Exception {
        mockMvc.perform(
                get("/items/" + INVALID_ID + "/delete")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_callBusinessLogic() throws Exception {
        ExecutionResult result = ExecutionResult.ok();
        Long id = Long.parseLong(VALID_ID);
        when(itemService.deleteItemById(id)).thenReturn(result);
        mockMvc.perform(
                get("/items/" + VALID_ID + "/delete")
        ).andExpect(status().is3xxRedirection());
        verify(itemService, times(1)).deleteItemById(id);
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_returnSuccessMessage() throws Exception {
        ExecutionResult result = ExecutionResult.ok();
        Long id = Long.parseLong(VALID_ID);
        when(itemService.deleteItemById(id)).thenReturn(result);
        mockMvc.perform(
                get("/items/" + VALID_ID + "/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"))
                .andExpect(flash().attribute(SUCCESS_MESSAGE, "Item deleted successfully"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenIdDoesNotExist_returnFailureMessage() throws Exception {
        ExecutionResult result = ExecutionResult.error(NO_OBJECT_FOUND, "No item found with id provided");
        Long id = Long.parseLong(VALID_ID);
        when(itemService.deleteItemById(id)).thenReturn(result);
        mockMvc.perform(
                get("/items/" + VALID_ID + "/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"))
                .andExpect(flash().attribute(FAILURE_MESSAGE, result.getErrorMessage()));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenInvalidRequest_returnErrorView() throws Exception {
        mockMvc.perform(
                get("/items/" + INVALID_ID + "/delete")
        ).andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString(ARGUMENT_TYPE_MISMATCH_MESSAGE)));
    }

}