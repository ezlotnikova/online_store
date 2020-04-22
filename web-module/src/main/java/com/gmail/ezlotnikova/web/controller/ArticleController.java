package com.gmail.ezlotnikova.web.controller;

import java.util.List;

import com.gmail.ezlotnikova.service.ArticleService;
import com.gmail.ezlotnikova.service.model.ArticlePreviewDTO;
import com.gmail.ezlotnikova.service.model.ArticleWithCommentsDTO;
import com.gmail.ezlotnikova.service.model.CommentDTO;
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
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model
    ) {
        Page<ArticlePreviewDTO> articles = articleService.findPaginatedAndOrderedByDate(page);
        model.addAttribute("articles", articles);
        return "article_previews";
    }

    @GetMapping("/{id}")
    public String showArticleWithComments(
            @PathVariable(name = "id") Long id,
            Model model
    ) {
        ArticleWithCommentsDTO article = articleService.findById(id);
        model.addAttribute("article", article);
        List<CommentDTO> comments = article.getComments();
        model.addAttribute("comments", comments);
        return "article_with_comments";
    }

}