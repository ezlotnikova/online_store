package com.gmail.ezlotnikova.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.ezlotnikova.service.UserService;
import com.gmail.ezlotnikova.service.model.AddUserDTO;
import com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserAPIController.class)
class UserAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidUser_returnStatusOk() throws Exception {
        AddUserDTO user = getValidUserDTO();
        String content = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        ).andExpect(status().isCreated());
    }

    private AddUserDTO getValidUserDTO() {
        AddUserDTO user = new AddUserDTO();
        user.setLastName(ControllerTestConstant.LAST_NAME);
        user.setFirstName(ControllerTestConstant.FIRST_NAME);
        user.setPatronymicName(ControllerTestConstant.PATRONYMIC_NAME);
        user.setEmail(ControllerTestConstant.EMAIL);
        user.setRole(ControllerTestConstant.ROLE);
        return user;
    }

}