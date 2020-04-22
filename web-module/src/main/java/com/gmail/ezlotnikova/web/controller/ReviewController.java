package com.gmail.ezlotnikova.web.controller;

import java.util.List;

import com.gmail.ezlotnikova.service.ReviewService;
import com.gmail.ezlotnikova.service.model.ShowReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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