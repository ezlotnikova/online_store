package com.gmail.ezlotnikova.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.ezlotnikova.service.UserService;
import com.gmail.ezlotnikova.service.model.AddUserDTO;
import com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserAPIController.class)
class UserAPIControllerAddUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidUser_returnStatusCreated() throws Exception {
        AddUserDTO user = getValidUserDTO();
        String content = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        ).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidUser_returnBadRequest() throws Exception {
        AddUserDTO user = getValidUserDTO();
        user.setLastName(null);
        String content = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidUser_callBusinessLogic() throws Exception {
        AddUserDTO user = getValidUserDTO();
        String content = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        ).andExpect(status().isCreated());
        ArgumentCaptor<AddUserDTO> userCaptor = ArgumentCaptor.forClass(AddUserDTO.class);
        verify(userService, times(1)).add(userCaptor.capture());
        Assertions.assertThat(userCaptor.getValue().equals(user));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidUser_returnUserWithId() throws Exception {
        AddUserDTO user = getValidUserDTO();
        String content = objectMapper.writeValueAsString(user);
        AddUserDTO addedUser = getValidUserDTO();
        addedUser.setId(1L);
        when(userService.add(eq(user))).thenReturn(addedUser);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        ).andReturn();
        String expectedReturnedContent = objectMapper.writeValueAsString(addedUser);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidUser_returnErrorMessage() throws Exception {
        AddUserDTO user = getValidUserDTO();
        user.setLastName(null);
        String content = objectMapper.writeValueAsString(user);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        ).andExpect(status().isBadRequest())
                .andReturn();
        APIExceptionHandler.ResponseError responseError = new APIExceptionHandler.ResponseError();
        responseError.setMessage("Is required");
        String expectedReturnedContent = objectMapper.writeValueAsString(responseError);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

    private AddUserDTO getValidUserDTO() {
        AddUserDTO user = new AddUserDTO();
        user.setLastName(ControllerTestConstant.USER_LAST_NAME);
        user.setFirstName(ControllerTestConstant.USER_FIRST_NAME);
        user.setPatronymicName(ControllerTestConstant.USER_PATRONYMIC_NAME);
        user.setEmail(ControllerTestConstant.USER_EMAIL);
        user.setRole(ControllerTestConstant.USER_ROLE);
        return user;
    }

}