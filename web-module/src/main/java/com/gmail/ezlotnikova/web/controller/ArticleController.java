package com.gmail.ezlotnikova.web.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gmail.ezlotnikova.service.ArticleService;
import com.gmail.ezlotnikova.service.model.ArticlePreviewDTO;
import com.gmail.ezlotnikova.service.model.ArticleWithCommentsDTO;
import com.gmail.ezlotnikova.service.model.CommentDTO;
import com.gmail.ezlotnikova.web.controller.constant.PaginationConstant;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping()
    public String showPreviewsByPage(
            Model model,
            @RequestParam("page") Optional<Integer> page) {
        int currentPage = page.orElse(1);
        int pageSize = PaginationConstant.ARTICLES_BY_PAGE;
        Page<ArticlePreviewDTO> articles = articleService.findPaginatedAndOrderedByDate(currentPage, pageSize);
        model.addAttribute("articles", articles);
        int totalPages = articles.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "article_previews";
    }

    @GetMapping("/{id}")
    public String showArticleWithComments(@PathVariable(name = "id") Long id,
            Model model
    ) {
        ArticleWithCommentsDTO article = articleService.findById(id);
        model.addAttribute("article", article);
        List<CommentDTO> comments = article.getComments();
        model.addAttribute("comments", comments);
        return "article_with_comments";
    }

}