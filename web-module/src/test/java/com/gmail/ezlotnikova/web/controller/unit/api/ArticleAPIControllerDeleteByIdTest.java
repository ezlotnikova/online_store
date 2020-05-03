package com.gmail.ezlotnikova.web.controller.unit.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.ezlotnikova.service.ArticleService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
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

import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.NO_OBJECT_FOUND;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.INVALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ExceptionHandlerConstant.ARGUMENT_TYPE_MISMATCH_MESSAGE;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleAPIController.class)
public class ArticleAPIControllerDeleteByIdTest {

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
                delete("/api/articles/" + VALID_ID)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidRequest_returnBadRequest() throws Exception {
        mockMvc.perform(
                delete("/api/articles/" + INVALID_ID)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidRequest_callBusinessLogic() throws Exception {
        mockMvc.perform(
                delete("/api/articles/" + VALID_ID)
        ).andExpect(status().isOk());
        Long id = Long.parseLong(VALID_ID);
        verify(articleService, times(1)).deleteArticleById(id);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidRequest_deleteArticle() throws Exception {
        ExecutionResult result = ExecutionResult.ok();
        Long id = Long.parseLong(VALID_ID);
        ExecutionResult operationResult = ExecutionResult.ok();
        when(articleService.deleteArticleById(id)).thenReturn(operationResult);

        MvcResult mvcResult = mockMvc.perform(
                delete("/api/articles/" + VALID_ID)
        ).andReturn();

        String expectedReturnedContent = objectMapper.writeValueAsString(result);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenIdDoesNotExist_returnMessage() throws Exception {
        Long id = Long.parseLong(VALID_ID);
        ExecutionResult result = ExecutionResult.error(NO_OBJECT_FOUND, "No article with id " + id + " found. ");
        ExecutionResult operationResult = ExecutionResult.error(NO_OBJECT_FOUND, "No article with id " + id + " found. ");
        when(articleService.deleteArticleById(id)).thenReturn(operationResult);

        MvcResult mvcResult = mockMvc.perform(
                delete("/api/articles/" + VALID_ID)
        ).andReturn();

        String expectedReturnedContent = objectMapper.writeValueAsString(result);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidRequest_returnErrorMessage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                delete("/api/articles/" + INVALID_ID)
        ).andReturn();
        APIExceptionHandler.ResponseError responseError = new APIExceptionHandler.ResponseError();
        responseError.setMessage(ARGUMENT_TYPE_MISMATCH_MESSAGE);
        String expectedReturnedContent = objectMapper.writeValueAsString(responseError);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

}