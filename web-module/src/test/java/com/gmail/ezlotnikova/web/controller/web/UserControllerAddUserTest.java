package com.gmail.ezlotnikova.web.controller.web;

import com.gmail.ezlotnikova.service.UserService;
import com.gmail.ezlotnikova.service.model.AddUserDTO;
import com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant;
import com.gmail.ezlotnikova.web.controller.web.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.NOT_EMPTY_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_EMAIL;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_FIRST_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_LAST_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_PATRONYMIC_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_ROLE;
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
public class UserControllerAddUserTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenRequest_returnAddUserForm() throws Exception {
        mockMvc.perform(
                get("/users/add")
        ).andExpect(status().isOk())
                .andExpect(view().name("user_add"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidUser_returnRedirectAndView() throws Exception {
        AddUserDTO user = getValidUserDTO();
        AddUserDTO addedUser = getValidUserDTO();
        addedUser.setId(1L);
        when(userService.add(user)).thenReturn(addedUser);
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", USER_LAST_NAME)
                        .param("firstName", USER_FIRST_NAME)
                        .param("patronymicName", USER_PATRONYMIC_NAME)
                        .param("email", USER_EMAIL)
                        .param("role", USER_ROLE.name())
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenInvalidUser_returnFormWithErrorMessage() throws Exception {
        AddUserDTO user = getValidUserDTO();
        AddUserDTO addedUser = getValidUserDTO();
        addedUser.setId(1L);
        when(userService.add(user)).thenReturn(addedUser);
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", "")
                        .param("firstName", USER_FIRST_NAME)
                        .param("patronymicName", USER_PATRONYMIC_NAME)
                        .param("email", USER_EMAIL)
                        .param("role", USER_ROLE.name())
        ).andExpect(status().isOk())
                .andExpect(view().name("user_add"))
                .andExpect(content().string(containsString(NOT_EMPTY_MESSAGE)));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidUser_callBusinessLogic() throws Exception {
        AddUserDTO user = getValidUserDTO();
        AddUserDTO addedUser = getValidUserDTO();
        addedUser.setId(1L);
        when(userService.add(user)).thenReturn(addedUser);
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", USER_LAST_NAME)
                        .param("firstName", USER_FIRST_NAME)
                        .param("patronymicName", USER_PATRONYMIC_NAME)
                        .param("email", USER_EMAIL)
                        .param("role", USER_ROLE.name())
        ).andExpect(status().is3xxRedirection());
        verify(userService, times(1)).add(user);
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidUser_showSuccessMessage() throws Exception {
        AddUserDTO user = getValidUserDTO();
        AddUserDTO addedUser = getValidUserDTO();
        addedUser.setId(1L);
        when(userService.add(user)).thenReturn(addedUser);
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", USER_LAST_NAME)
                        .param("firstName", USER_FIRST_NAME)
                        .param("patronymicName", USER_PATRONYMIC_NAME)
                        .param("email", USER_EMAIL)
                        .param("role", USER_ROLE.name())
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute(
                        SUCCESS_MESSAGE, "User was added successfully"));
    }

    private AddUserDTO getValidUserDTO() {
        AddUserDTO user = new AddUserDTO();
        user.setLastName(USER_LAST_NAME);
        user.setFirstName(ControllerTestConstant.USER_FIRST_NAME);
        user.setPatronymicName(ControllerTestConstant.USER_PATRONYMIC_NAME);
        user.setEmail(ControllerTestConstant.USER_EMAIL);
        user.setRole(ControllerTestConstant.USER_ROLE);
        return user;
    }

}