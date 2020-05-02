package com.gmail.ezlotnikova.web.controller.web;

import java.util.ArrayList;
import java.util.List;

import com.gmail.ezlotnikova.service.UserService;
import com.gmail.ezlotnikova.service.model.ShowUserDTO;
import com.gmail.ezlotnikova.web.controller.web.UserController;
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
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.OBJECTS_BY_PAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_EMAIL;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_FIRST_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_LAST_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_PATRONYMIC_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_ROLE;
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

@WebMvcTest(controllers = UserController.class)
public class UserControllerShowAllTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_returnView() throws Exception {
        Page<ShowUserDTO> users = getPageOfUsers();
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        when(userService.findPaginatedAndOrderedByEmail(pageNumber)).thenReturn(users);
        mockMvc.perform(
                get("/users")
                        .param("page", VALID_PAGE_NUMBER)
        ).andExpect(status().isOk())
                .andExpect(view().name("users"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenInvalidRequest_returnBadRequest() throws Exception {
        mockMvc.perform(
                get("/users")
                        .param("page", INVALID_PAGE_NUMBER)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_callBusinessLogic() throws Exception {
        Page<ShowUserDTO> users = getPageOfUsers();
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        when(userService.findPaginatedAndOrderedByEmail(pageNumber)).thenReturn(users);
        mockMvc.perform(
                get("/users")
                        .param("page", VALID_PAGE_NUMBER)
        ).andExpect(status().isOk());
        verify(userService, times(1)).findPaginatedAndOrderedByEmail(pageNumber);
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_returnViewWithUsers() throws Exception {
        Page<ShowUserDTO> users = getPageOfUsers();
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        when(userService.findPaginatedAndOrderedByEmail(pageNumber)).thenReturn(users);
        mockMvc.perform(
                get("/users")
                        .param("page", VALID_PAGE_NUMBER)
        ).andExpect(status().isOk())
                .andExpect(content().string(containsString(USER_FIRST_NAME)))
                .andExpect(content().string(containsString(USER_ROLE.name())));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenInvalidRequest_returnErrorView() throws Exception {
        mockMvc.perform(
                get("/users")
                        .param("page", INVALID_PAGE_NUMBER)
        ).andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString(ARGUMENT_TYPE_MISMATCH_MESSAGE)));
    }

    private Page<ShowUserDTO> getPageOfUsers() {
        ShowUserDTO user = getShowUserDTO();
        List<ShowUserDTO> userList = new ArrayList<>();
        userList.add(user);
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, OBJECTS_BY_PAGE);
        return new PageImpl<>(userList, pageRequest, 1);
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