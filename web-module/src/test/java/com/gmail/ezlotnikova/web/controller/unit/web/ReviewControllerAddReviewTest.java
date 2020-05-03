package com.gmail.ezlotnikova.web.controller.unit.web;

import com.gmail.ezlotnikova.service.ReviewService;
import com.gmail.ezlotnikova.service.model.AddReviewDTO;
import com.gmail.ezlotnikova.service.model.UserDTO;
import com.gmail.ezlotnikova.service.security.AppUser;
import com.gmail.ezlotnikova.web.controller.web.ReviewController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.NOT_EMPTY_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_ROLE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_REVIEW_TEXT;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.SUCCESS_MESSAGE;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = ReviewController.class)
public class ReviewControllerAddReviewTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private ReviewService reviewService;

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenRequest_returnAddReviewForm() throws Exception {
        mockMvc.perform(
                get("/reviews/add")
        ).andExpect(status().isOk())
                .andExpect(view().name("review_add"))
                .andExpect(model().attributeExists("review"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidReview_returnRedirectAndView() throws Exception {
        AppUser appUser = getAppUser();
        AddReviewDTO review = getValidReviewDTO();
        AddReviewDTO addedReview = getValidReviewDTO();
        addedReview.setId(1L);
        when(reviewService.add(review)).thenReturn(addedReview);
        mockMvc.perform(
                post("/reviews/add")
                        .with(user(appUser))
                        .param("reviewText", VALID_REVIEW_TEXT)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenInvalidReview_returnFormWithErrorMessage() throws Exception {
        AppUser appUser = getAppUser();
        AddReviewDTO review = getValidReviewDTO();
        AddReviewDTO addedReview = getValidReviewDTO();
        addedReview.setId(1L);
        when(reviewService.add(review)).thenReturn(addedReview);
        mockMvc.perform(
                post("/reviews/add")
                        .with(user(appUser))
                        .param("reviewText", "")
        ).andExpect(status().isOk())
                .andExpect(view().name("review_add"))
                .andExpect(content().string(containsString(NOT_EMPTY_MESSAGE)));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidReview_callBusinessLogic() throws Exception {
        AppUser appUser = getAppUser();
        AddReviewDTO review = getValidReviewDTO();
        review.setUserId(appUser.getId());
        AddReviewDTO addedReview = getValidReviewDTO();
        addedReview.setId(1L);
        when(reviewService.add(review)).thenReturn(addedReview);
        mockMvc.perform(
                post("/reviews/add")
                        .with(user(appUser))
                        .param("reviewText", VALID_REVIEW_TEXT)
        ).andExpect(status().is3xxRedirection());
        verify(reviewService, times(1)).add(review);
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidReview_showSuccessMessage() throws Exception {
        AppUser appUser = getAppUser();
        AddReviewDTO review = getValidReviewDTO();
        AddReviewDTO addedReview = getValidReviewDTO();
        addedReview.setId(1L);
        when(reviewService.add(review)).thenReturn(addedReview);
        mockMvc.perform(
                post("/reviews/add")
                        .with(user(appUser))
                        .param("reviewText", VALID_REVIEW_TEXT)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews"))
                .andExpect(flash().attribute(
                        SUCCESS_MESSAGE, "Thank you for your opinion! Your review will appear on our site after moderation."));
    }

    private AppUser getAppUser() {
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setRole(USER_ROLE);
        return new AppUser(user);
    }

    private AddReviewDTO getValidReviewDTO() {
        AddReviewDTO review = new AddReviewDTO();
        review.setReviewText(VALID_REVIEW_TEXT);
        return review;
    }

}