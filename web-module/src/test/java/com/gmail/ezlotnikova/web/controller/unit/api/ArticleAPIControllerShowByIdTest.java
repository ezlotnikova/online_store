package com.gmail.ezlotnikova.web.controller.unit.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.ezlotnikova.service.ArticleService;
import com.gmail.ezlotnikova.service.model.ArticleWithCommentsDTO;
import com.gmail.ezlotnikova.service.model.CommentDTO;
import com.gmail.ezlotnikova.web.controller.api.APIExceptionHandler;
import com.gmail.ezlotnikova.web.controller.api.ArticleAPIController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.DATE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.INVALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_FIRST_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_LAST_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ARTICLE_CONTENT;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ARTICLE_HEADER;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_COMMENT_CONTENT;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ExceptionHandlerConstant.ARGUMENT_TYPE_MISMATCH_MESSAGE;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleAPIController.class)
public class ArticleAPIControllerShowByIdTest {

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
                get("/api/articles/" + VALID_ID)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidRequest_returnBadRequest() throws Exception {
        mockMvc.perform(
                get("/api/articles/" + INVALID_ID)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidRequest_callBusinessLogic() throws Exception {
        mockMvc.perform(
                get("/api/articles/" + VALID_ID)
        ).andExpect(status().isOk());
        Long id = Long.parseLong(VALID_ID);
        verify(articleService, times(1)).findArticleWithCommentsById(id);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidRequest_returnArticle() throws Exception {
        ArticleWithCommentsDTO article = getArticleWithCommentsDTO();
        Long id = Long.parseLong(VALID_ID);
        when(articleService.findArticleWithCommentsById(id)).thenReturn(article);

        MvcResult mvcResult = mockMvc.perform(
                get("/api/articles/" + VALID_ID)
        ).andReturn();

        String expectedReturnedContent = objectMapper.writeValueAsString(article);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidRequest_returnErrorMessage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/api/articles/" + INVALID_ID)
        ).andReturn();
        APIExceptionHandler.ResponseError responseError = new APIExceptionHandler.ResponseError();
        responseError.setMessage(ARGUMENT_TYPE_MISMATCH_MESSAGE);
        String expectedReturnedContent = objectMapper.writeValueAsString(responseError);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

    private ArticleWithCommentsDTO getArticleWithCommentsDTO() {
        ArticleWithCommentsDTO article = new ArticleWithCommentsDTO();
        article.setId(1L);
        article.setAuthorFirstName(USER_FIRST_NAME);
        article.setAuthorLastName(USER_LAST_NAME);
        article.setHeader(VALID_ARTICLE_HEADER);
        article.setContent(VALID_ARTICLE_CONTENT);
        article.setDate(DATE);
        CommentDTO comment = new CommentDTO();
        comment.setId(1L);
        comment.setAuthorFirstName(USER_FIRST_NAME);
        comment.setAuthorLastName(USER_LAST_NAME);
        comment.setCreatedOn(DATE);
        comment.setContent(VALID_COMMENT_CONTENT);
        List<CommentDTO> comments = new ArrayList<>();
        comments.add(comment);
        article.setComments(comments);
        return article;
    }

}