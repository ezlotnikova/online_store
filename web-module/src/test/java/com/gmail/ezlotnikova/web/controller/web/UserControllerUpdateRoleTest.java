package com.gmail.ezlotnikova.web.controller.web;

import com.gmail.ezlotnikova.service.UserService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.ShowUserDTO;
import com.gmail.ezlotnikova.web.controller.web.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.NO_OBJECT_FOUND;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.INVALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_EMAIL;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_FIRST_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_LAST_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_PATRONYMIC_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_ROLE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ExceptionHandlerConstant.ARGUMENT_TYPE_MISMATCH_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.FAILURE_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.SUCCESS_MESSAGE;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = UserController.class)
public class UserControllerUpdateRoleTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenRequest_returnAddUserForm() throws Exception {
        ShowUserDTO user = getShowUserDTO();
        Long id = Long.parseLong(VALID_ID);
        when(userService.findUserById(id)).thenReturn(user);
        mockMvc.perform(
                get("/users/" + VALID_ID)
        ).andExpect(status().isOk())
                .andExpect(view().name("user_change_role"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("roles"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_returnRedirect() throws Exception {
        ExecutionResult result = ExecutionResult.ok();
        Long id = Long.parseLong(VALID_ID);
        when(userService.updateUserRoleById(id, USER_ROLE)).thenReturn(result);
        mockMvc.perform(
                post("/users/" + VALID_ID)
                        .param("newRole", USER_ROLE.name())
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenInvalidRequest_returnBadRequest() throws Exception {
        mockMvc.perform(
                post("/users/" + INVALID_ID)
                        .param("newRole", USER_ROLE.name())
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_callBusinessLogic() throws Exception {
        ExecutionResult result = ExecutionResult.ok();
        Long id = Long.parseLong(VALID_ID);
        when(userService.updateUserRoleById(id, USER_ROLE)).thenReturn(result);
        mockMvc.perform(
                post("/users/" + VALID_ID)
                        .param("newRole", USER_ROLE.name())
        ).andExpect(status().is3xxRedirection());
        verify(userService, times(1)).updateUserRoleById(id, USER_ROLE);
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_returnSuccessMessage() throws Exception {
        ExecutionResult result = ExecutionResult.ok();
        Long id = Long.parseLong(VALID_ID);
        when(userService.updateUserRoleById(id, USER_ROLE)).thenReturn(result);
        mockMvc.perform(
                post("/users/" + VALID_ID)
                        .param("newRole", USER_ROLE.name())
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute(SUCCESS_MESSAGE, "User's role updated successfully"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenIdDoesNotExist_returnFailureMessage() throws Exception {
        Long id = Long.parseLong(VALID_ID);
        ExecutionResult result = ExecutionResult.error(NO_OBJECT_FOUND, "No user with id " + id + " found. ");
        when(userService.updateUserRoleById(id, USER_ROLE)).thenReturn(result);
        mockMvc.perform(
                post("/users/" + VALID_ID)
                        .param("newRole", USER_ROLE.name())
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute(FAILURE_MESSAGE, result.getErrorMessage()));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenInvalidRequest_returnErrorView() throws Exception {
        mockMvc.perform(
                post("/users/" + INVALID_ID)
                        .param("newRole", USER_ROLE.name())
        ).andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString(ARGUMENT_TYPE_MISMATCH_MESSAGE)));
    }

    private ShowUserDTO getShowUserDTO() {
        ShowUserDTO user = new ShowUserDTO();
        user.setId(1L);
        user.setLastName(USER_LAST_NAME);
        user.setFirstName(USER_FIRST_NAME);
        user.setPatronymicName(USER_PATRONYMIC_NAME);
        user.setEmail(USER_EMAIL);
        user.setRole(USER_ROLE);
        return user;
    }

}