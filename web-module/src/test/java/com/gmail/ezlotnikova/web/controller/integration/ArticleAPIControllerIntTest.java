package com.gmail.ezlotnikova.web.controller.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_PAGE_NUMBER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(value = "/integration-test.properties")
public class ArticleAPIControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getArticles_returnAllArticles() throws Exception {
        mockMvc.perform(
                get("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", VALID_PAGE_NUMBER))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].header").value("TestHeader"))
                .andExpect(jsonPath("$.content[0].contentPreview").value("TestContent"));
    }

}