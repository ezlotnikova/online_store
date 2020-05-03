package com.gmail.ezlotnikova.web.controller.unit.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.ezlotnikova.service.ArticleService;
import com.gmail.ezlotnikova.service.model.ArticlePreviewDTO;
import com.gmail.ezlotnikova.web.controller.api.APIExceptionHandler;
import com.gmail.ezlotnikova.web.controller.api.ArticleAPIController;
import org.assertj.core.api.Assertions;
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
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.DATE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.INVALID_PAGE_NUMBER;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.OBJECTS_BY_PAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_FIRST_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_LAST_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ARTICLE_CONTENT_PREVIEW;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ARTICLE_HEADER;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_PAGE_NUMBER;
import static com.gmail.ezlotnikova.web.controller.constant.ExceptionHandlerConstant.ARGUMENT_TYPE_MISMATCH_MESSAGE;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleAPIController.class)
public class ArticleAPIControllerShowAllTest {

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
    void whenValidRequest_returnStatusOk() throws Exception {
        mockMvc.perform(
                get("/api/articles")
                        .param("page", VALID_PAGE_NUMBER)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidRequest_returnBadRequest() throws Exception {
        mockMvc.perform(
                get("/api/articles")
                        .param("page", INVALID_PAGE_NUMBER)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidRequest_callBusinessLogic() throws Exception {
        mockMvc.perform(
                get("/api/articles")
                        .param("page", VALID_PAGE_NUMBER)
        ).andExpect(status().isOk());
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        verify(articleService, times(1)).findPaginatedAndOrderedByDate(pageNumber);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidRequest_returnPageOfArticles() throws Exception {
        ArticlePreviewDTO article = getArticlePreviewDTO();
        List<ArticlePreviewDTO> articleList = new ArrayList<>();
        articleList.add(article);
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, OBJECTS_BY_PAGE);
        Page<ArticlePreviewDTO> articles = new PageImpl<>(articleList, pageRequest, 1);
        when(articleService.findPaginatedAndOrderedByDate(pageNumber)).thenReturn(articles);

        MvcResult mvcResult = mockMvc.perform(
                get("/api/articles")
                        .param("page", VALID_PAGE_NUMBER)
        ).andReturn();

        String expectedReturnedContent = objectMapper.writeValueAsString(articles);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidRequest_returnErrorMessage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/api/articles")
                        .param("page", INVALID_PAGE_NUMBER)
        ).andReturn();
        APIExceptionHandler.ResponseError responseError = new APIExceptionHandler.ResponseError();
        responseError.setMessage(ARGUMENT_TYPE_MISMATCH_MESSAGE);
        String expectedReturnedContent = objectMapper.writeValueAsString(responseError);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

    private ArticlePreviewDTO getArticlePreviewDTO() {
        ArticlePreviewDTO article = new ArticlePreviewDTO();
        article.setId(1L);
        article.setAuthorFirstName(USER_FIRST_NAME);
        article.setAuthorLastName(USER_LAST_NAME);
        article.setHeader(VALID_ARTICLE_HEADER);
        article.setContentPreview(VALID_ARTICLE_CONTENT_PREVIEW);
        article.setDate(DATE);
        return article;
    }

}