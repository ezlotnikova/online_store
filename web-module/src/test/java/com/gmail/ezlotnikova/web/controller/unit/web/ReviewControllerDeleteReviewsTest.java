package com.gmail.ezlotnikova.web.controller.unit.web;

import com.gmail.ezlotnikova.service.ReviewService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.web.controller.web.ReviewController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.NO_OBJECT_FOUND;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.INVALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ExceptionHandlerConstant.ARGUMENT_TYPE_MISMATCH_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.FAILURE_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.SUCCESS_MESSAGE;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = ReviewController.class)
public class ReviewControllerDeleteReviewsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private ReviewService reviewService;

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_returnRedirectAndView() throws Exception {
        Long id = Long.parseLong(VALID_ID);
        String[] idList = {VALID_ID};
        ExecutionResult result = ExecutionResult.ok();
        when(reviewService.deleteById(id)).thenReturn(result);
        mockMvc.perform(
                post("/reviews/delete")
                        .param("idList", idList)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenNoParam_returnRedirectAndView() throws Exception {
        mockMvc.perform(
                post("/reviews/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_callBusinessLogic() throws Exception {
        Long id = Long.parseLong(VALID_ID);
        String[] idList = {VALID_ID};
        ExecutionResult result = ExecutionResult.ok();
        when(reviewService.deleteById(id)).thenReturn(result);
        mockMvc.perform(
                post("/reviews/delete")
                        .param("idList", idList)
        ).andExpect(status().is3xxRedirection());
        Mockito.verify(reviewService, times(idList.length)).deleteById(id);
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_returnSuccessMessage() throws Exception {
        Long id = Long.parseLong(VALID_ID);
        String[] idList = {VALID_ID};
        ExecutionResult result = ExecutionResult.ok();
        when(reviewService.deleteById(id)).thenReturn(result);
        mockMvc.perform(
                post("/reviews/delete")
                        .param("idList", idList)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews"))
                .andExpect(flash().attribute(SUCCESS_MESSAGE, "Review(s) deleted successfully"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenIdDoesNotExist_returnFailureMessage() throws Exception {
        Long id = Long.parseLong(VALID_ID);
        String[] idList = {VALID_ID};
        ExecutionResult result = ExecutionResult.error(NO_OBJECT_FOUND, "No review with id " + id + " found. ");
        when(reviewService.deleteById(id)).thenReturn(result);
        mockMvc.perform(
                post("/reviews/delete")
                        .param("idList", idList)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews"))
                .andExpect(flash().attribute(FAILURE_MESSAGE, result.getErrorMessage()));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenInvalidRequest_returnErrorView() throws Exception {
        String[] idList = {INVALID_ID};
        mockMvc.perform(
                post("/reviews/delete")
                        .param("idList", idList)
        ).andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString(ARGUMENT_TYPE_MISMATCH_MESSAGE)));
    }

}