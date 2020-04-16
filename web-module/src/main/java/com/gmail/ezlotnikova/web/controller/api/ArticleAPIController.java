package com.gmail.ezlotnikova.web.controller.api;

import java.util.Optional;
import javax.validation.Valid;

import com.gmail.ezlotnikova.service.ArticleService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.AddArticleDTO;
import com.gmail.ezlotnikova.service.model.ArticlePreviewDTO;
import com.gmail.ezlotnikova.service.model.ArticleWithCommentsDTO;
import com.gmail.ezlotnikova.web.controller.constant.PaginationConstant;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    public Page<ArticlePreviewDTO> showAllItems(@RequestParam("page") Optional<Integer> page) {
        int currentPage = page.orElse(1);
        int pageSize = PaginationConstant.ARTICLES_BY_PAGE;
        return articleService.findPaginatedAndOrderedByDate(currentPage, pageSize);
    }

    @PostMapping
    public AddArticleDTO addArticle(
            @Valid @RequestBody AddArticleDTO articleDTO) {
        return articleService.add(articleDTO);
    }

    @GetMapping("/{id}")
    public ArticleWithCommentsDTO showArticleWithComments(@PathVariable(name = "id") Long id) {
        return articleService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ExecutionResult deleteArticleById(@PathVariable(name = "id") Long id) {
        return articleService.deleteArticleById(id);
    }

}