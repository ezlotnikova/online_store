package com.gmail.ezlotnikova.web.controller;

import java.util.List;
import javax.validation.Valid;

import com.gmail.ezlotnikova.service.ReviewService;
import com.gmail.ezlotnikova.service.model.AddReviewDTO;
import com.gmail.ezlotnikova.service.model.ShowReviewDTO;
import com.gmail.ezlotnikova.service.security.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.SUCCESS_MESSAGE;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping()
    public String showReviewsByPage(
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model
    ) {
        Page<ShowReviewDTO> reviews = reviewService.findPaginated(page);
        model.addAttribute("reviews", reviews);
        return "reviews";
    }

    @GetMapping("/add")
    public String showAddReviewForm(Model model) {
        AddReviewDTO review = new AddReviewDTO();
        model.addAttribute("review", review);
        return "review_add";
    }

    @PostMapping("/add")
    public String addNewReview(
            @AuthenticationPrincipal AppUser appUser,
            @Valid @ModelAttribute(name = "review") AddReviewDTO review,
            BindingResult errors,
            RedirectAttributes redirectAttributes
    ) {
        if (errors.hasErrors()) {
            return "review_add";
        } else {
            review.setUserId(
                    appUser.getId());
            reviewService.add(review);
            redirectAttributes.addFlashAttribute(
                    SUCCESS_MESSAGE, "Thank you for your opinion! Your review will appear on our site after moderation.");
            return "redirect:/reviews/add";
        }
    }

    @PostMapping("/delete")
    public String deleteSelectedReviews(
            @RequestParam(name = "idList", required = false) List<Long> idList
    ) {
        if (idList != null) {
            idList.forEach(reviewService::deleteById);
        }
        return "redirect:/reviews";
    }

}