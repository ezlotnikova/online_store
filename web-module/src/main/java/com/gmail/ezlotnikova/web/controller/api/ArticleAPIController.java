package com.gmail.ezlotnikova.web.controller.api;

import javax.validation.Valid;

import com.gmail.ezlotnikova.service.ArticleService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.AddArticleDTO;
import com.gmail.ezlotnikova.service.model.ArticlePreviewDTO;
import com.gmail.ezlotnikova.service.model.ArticleWithCommentsDTO;
import com.gmail.ezlotnikova.service.security.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/articles")
public class ArticleAPIController {

    private final ArticleService articleService;

    public ArticleAPIController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public Page<ArticlePreviewDTO> showAllArticles(
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        return articleService.findPaginatedAndOrderedByDate(page);
    }

    @GetMapping("/{id}")
    public ArticleWithCommentsDTO showArticleWithComments(
            @PathVariable(name = "id") Long id
    ) {
        return articleService.findArticleWithCommentsById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddArticleDTO addArticle(
            @AuthenticationPrincipal AppUser appUser,
            @Valid @RequestBody AddArticleDTO article
    ) {
        if (article.getAuthorEmail() == null) {
            String email = appUser.getUsername();
            article.setAuthorEmail(email);
        }
        return articleService.add(article);
    }

    @DeleteMapping("/{id}")
    public ExecutionResult deleteArticleById(
            @PathVariable(name = "id") Long id
    ) {
        return articleService.deleteArticleById(id);
    }

}