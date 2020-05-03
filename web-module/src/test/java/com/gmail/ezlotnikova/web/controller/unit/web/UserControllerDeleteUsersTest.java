package com.gmail.ezlotnikova.web.controller.unit.web;

import com.gmail.ezlotnikova.service.UserService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.web.controller.web.UserController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.ACTION_FORBIDDEN;
import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.NO_OBJECT_FOUND;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.INVALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ExceptionHandlerConstant.ARGUMENT_TYPE_MISMATCH_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.FAILURE_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.SUCCESS_MESSAGE;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = UserController.class)
public class UserControllerDeleteUsersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_returnRedirectAndView() throws Exception {
        Long id = Long.parseLong(VALID_ID);
        String[] idList = {VALID_ID};
        ExecutionResult result = ExecutionResult.ok();
        when(userService.deleteById(id)).thenReturn(result);
        mockMvc.perform(
                post("/users/delete")
                        .param("idList", idList)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenNoParam_returnRedirectAndView() throws Exception {
        mockMvc.perform(
                post("/users/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_callBusinessLogic() throws Exception {
        Long id = Long.parseLong(VALID_ID);
        String[] idList = {VALID_ID};
        ExecutionResult result = ExecutionResult.ok();
        when(userService.deleteById(id)).thenReturn(result);
        mockMvc.perform(
                post("/users/delete")
                        .param("idList", idList)
        ).andExpect(status().is3xxRedirection());
        Mockito.verify(userService, times(idList.length)).deleteById(id);
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_returnSuccessMessage() throws Exception {
        Long id = Long.parseLong(VALID_ID);
        String[] idList = {VALID_ID};
        ExecutionResult result = ExecutionResult.ok();
        when(userService.deleteById(id)).thenReturn(result);
        mockMvc.perform(
                post("/users/delete")
                        .param("idList", idList)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute(SUCCESS_MESSAGE, "User(s) deleted successfully"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenUnableToDelete_returnFailureMessage() throws Exception {
        Long id = Long.parseLong(VALID_ID);
        String[] idList = {VALID_ID};
        ExecutionResult result = ExecutionResult.error(ACTION_FORBIDDEN, "Deleting the only administrator is forbidden. ");
        when(userService.deleteById(id)).thenReturn(result);
        mockMvc.perform(
                post("/users/delete")
                        .param("idList", idList)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute(FAILURE_MESSAGE, result.getErrorMessage()));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenIdDoesNotExist_returnFailureMessage() throws Exception {
        Long id = Long.parseLong(VALID_ID);
        String[] idList = {VALID_ID};
        ExecutionResult result = ExecutionResult.error(NO_OBJECT_FOUND, "No user with id " + id + " found. ");
        when(userService.deleteById(id)).thenReturn(result);
        mockMvc.perform(
                post("/users/delete")
                        .param("idList", idList)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute(FAILURE_MESSAGE, result.getErrorMessage()));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenInvalidRequest_returnErrorView() throws Exception {
        String[] idList = {INVALID_ID};
        mockMvc.perform(
                post("/users/delete")
                        .param("idList", idList)
        ).andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString(ARGUMENT_TYPE_MISMATCH_MESSAGE)));
    }

}