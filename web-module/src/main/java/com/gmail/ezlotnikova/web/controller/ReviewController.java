package com.gmail.ezlotnikova.web.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gmail.ezlotnikova.service.ReviewService;
import com.gmail.ezlotnikova.service.model.ShowReviewDTO;
import com.gmail.ezlotnikova.web.controller.constant.PaginationConstant;
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
            Model model,
            @RequestParam("page") Optional<Integer> page
    ) {
        int currentPage = page.orElse(1);
        int pageSize = PaginationConstant.REVIEWS_BY_PAGE;
        Page<ShowReviewDTO> reviews = reviewService.findPaginated(currentPage, pageSize);
        model.addAttribute("reviews", reviews);
        int totalPages = reviews.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "reviews";
    }

    @PostMapping("/delete")
    public String deleteSelectedReviews(
            @RequestParam(name = "idList", required = false) List<String> idList) {
        if (idList != null) {
            idList.stream()
                    .map(Long::parseLong)
                    .forEach(reviewService::deleteById);
        }
        return "redirect:/reviews";
    }

}