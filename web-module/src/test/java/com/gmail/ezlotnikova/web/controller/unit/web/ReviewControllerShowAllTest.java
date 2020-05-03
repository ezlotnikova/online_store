package com.gmail.ezlotnikova.web.controller.unit.web;

import java.util.ArrayList;
import java.util.List;

import com.gmail.ezlotnikova.service.ReviewService;
import com.gmail.ezlotnikova.service.model.ShowReviewDTO;
import com.gmail.ezlotnikova.web.controller.web.ReviewController;
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

import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.DATE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.INVALID_PAGE_NUMBER;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.OBJECTS_BY_PAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_FIRST_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_LAST_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_PATRONYMIC_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_PAGE_NUMBER;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_REVIEW_TEXT;
import static com.gmail.ezlotnikova.web.controller.constant.ExceptionHandlerConstant.ARGUMENT_TYPE_MISMATCH_MESSAGE;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = ReviewController.class)
public class ReviewControllerShowAllTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private ReviewService reviewService;

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_returnView() throws Exception {
        Page<ShowReviewDTO> reviews = getPageOfReviews();
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        when(reviewService.findPaginated(pageNumber)).thenReturn(reviews);
        mockMvc.perform(
                get("/reviews")
                        .param("page", VALID_PAGE_NUMBER)
        ).andExpect(status().isOk())
                .andExpect(view().name("reviews"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenInvalidRequest_returnBadRequest() throws Exception {
        mockMvc.perform(
                get("/reviews")
                        .param("page", INVALID_PAGE_NUMBER)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_callBusinessLogic() throws Exception {
        Page<ShowReviewDTO> reviews = getPageOfReviews();
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        when(reviewService.findPaginated(pageNumber)).thenReturn(reviews);
        mockMvc.perform(
                get("/reviews")
                        .param("page", VALID_PAGE_NUMBER)
        ).andExpect(status().isOk());
        verify(reviewService, times(1)).findPaginated(pageNumber);
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_returnViewWithReviews() throws Exception {
        Page<ShowReviewDTO> reviews = getPageOfReviews();
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        when(reviewService.findPaginated(pageNumber)).thenReturn(reviews);
        mockMvc.perform(
                get("/reviews")
                        .param("page", VALID_PAGE_NUMBER)
        ).andExpect(status().isOk())
                .andExpect(content().string(containsString(VALID_REVIEW_TEXT)));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenInvalidRequest_returnErrorView() throws Exception {
        mockMvc.perform(
                get("/reviews")
                        .param("page", INVALID_PAGE_NUMBER)
        ).andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString(ARGUMENT_TYPE_MISMATCH_MESSAGE)));
    }

    private Page<ShowReviewDTO> getPageOfReviews() {
        ShowReviewDTO review = getShowReviewDTO();
        List<ShowReviewDTO> reviewList = new ArrayList<>();
        reviewList.add(review);
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, OBJECTS_BY_PAGE);
        return new PageImpl<>(reviewList, pageRequest, 1);
    }

    private ShowReviewDTO getShowReviewDTO() {
        ShowReviewDTO review = new ShowReviewDTO();
        review.setId(1L);
        review.setLastName(USER_LAST_NAME);
        review.setFirstName(USER_FIRST_NAME);
        review.setPatronymicName(USER_PATRONYMIC_NAME);
        review.setReviewText(VALID_REVIEW_TEXT);
        review.setCreatedOn(DATE);
        review.setVisible(false);
        return review;
    }

}