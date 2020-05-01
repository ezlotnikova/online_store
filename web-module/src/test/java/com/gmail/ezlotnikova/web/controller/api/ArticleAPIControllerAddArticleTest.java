package com.gmail.ezlotnikova.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.ezlotnikova.service.ArticleService;
import com.gmail.ezlotnikova.service.model.AddArticleDTO;
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

import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.DATE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_EMAIL;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ARTICLE_CONTENT;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ARTICLE_HEADER;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleAPIController.class)
class ArticleAPIControllerAddArticleTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private ArticleService articleService;

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidArticle_returnStatusOk() throws Exception {
        AddArticleDTO article = getValidArticleDTO();
        String content = objectMapper.writeValueAsString(article);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        ).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidArticle_returnBadRequest() throws Exception {
        AddArticleDTO article = getValidArticleDTO();
        article.setHeader(null);
        String content = objectMapper.writeValueAsString(article);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidArticle_callBusinessLogic() throws Exception {
        AddArticleDTO article = getValidArticleDTO();
        String content = objectMapper.writeValueAsString(article);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        ).andExpect(status().isCreated());
        ArgumentCaptor<AddArticleDTO> articleCaptor = ArgumentCaptor.forClass(AddArticleDTO.class);
        verify(articleService, times(1)).add(articleCaptor.capture());
        Assertions.assertThat(articleCaptor.getValue().equals(article));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidArticle_returnArticleWithId() throws Exception {
        AddArticleDTO article = getValidArticleDTO();
        String content = objectMapper.writeValueAsString(article);
        AddArticleDTO addedArticle = getValidArticleDTO();
        addedArticle.setId(1L);
        when(articleService.add(eq(article))).thenReturn(addedArticle);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        ).andReturn();
        String expectedReturnedContent = objectMapper.writeValueAsString(addedArticle);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidArticle_returnErrorMessage() throws Exception {
        AddArticleDTO article = getValidArticleDTO();
        article.setHeader(null);
        String content = objectMapper.writeValueAsString(article);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/articles")
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

    private AddArticleDTO getValidArticleDTO() {
        AddArticleDTO article = new AddArticleDTO();
        article.setAuthorEmail(USER_EMAIL);
        article.setHeader(VALID_ARTICLE_HEADER);
        article.setContent(VALID_ARTICLE_CONTENT);
        article.setDate(DATE);
        return article;
    }

}