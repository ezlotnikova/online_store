package com.gmail.ezlotnikova.web.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.ezlotnikova.service.model.AddUserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_EMAIL;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_FIRST_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_LAST_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_PATRONYMIC_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_ROLE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(value = "/integration-test.properties")
public class UserAPIControllerIntTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addUser_returnAddedUser() throws Exception {
        AddUserDTO user = getValidUserDTO();
        String content = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.email").value(USER_EMAIL));
    }

    private AddUserDTO getValidUserDTO() {
        AddUserDTO user = new AddUserDTO();
        user.setLastName(USER_LAST_NAME);
        user.setFirstName(USER_FIRST_NAME);
        user.setPatronymicName(USER_PATRONYMIC_NAME);
        user.setEmail(USER_EMAIL);
        user.setRole(USER_ROLE);
        return user;
    }

}