package com.gmail.ezlotnikova.web.controller.web;

import java.util.List;
import javax.validation.Valid;

import com.gmail.ezlotnikova.service.ArticleService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.AddArticleDTO;
import com.gmail.ezlotnikova.service.model.AddCommentDTO;
import com.gmail.ezlotnikova.service.model.ArticlePreviewDTO;
import com.gmail.ezlotnikova.service.model.ArticleWithCommentsDTO;
import com.gmail.ezlotnikova.service.model.CommentDTO;
import com.gmail.ezlotnikova.service.security.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.gmail.ezlotnikova.service.constant.ResultTypeEnum.EXECUTED_SUCCESSFULLY;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.FAILURE_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.SUCCESS_MESSAGE;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/add")
    public String showAddArticleForm(
            Model model
    ) {
        AddArticleDTO article = new AddArticleDTO();
        model.addAttribute("article", article);
        return "article_add";
    }

    @PostMapping("/add")
    public String addNewArticle(
            @AuthenticationPrincipal AppUser appUser,
            @Valid @ModelAttribute(name = "article") AddArticleDTO article,
            BindingResult errors,
            RedirectAttributes redirectAttributes
    ) {
        if (errors.hasErrors()) {
            return "article_add";
        } else {
            String email = appUser.getUsername();
            article.setAuthorEmail(email);
            articleService.add(article);
            redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Article added successfully");
            return "redirect:/articles";
        }
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
        ArticleWithCommentsDTO article = articleService.findArticleWithCommentsById(id);
        if (article != null) {
            List<CommentDTO> comments = article.getComments();
            model.addAttribute("article", article);
            model.addAttribute("comments", comments);
            return "article_with_comments";
        } else {
            WebExceptionHandler.ResponseError error = new WebExceptionHandler.ResponseError();
            error.setMessage("No article found");
            model.addAttribute("error", error);
            return "error";
        }
    }

    @GetMapping("{id}/edit")
    public String showEditArticleForm(
            @PathVariable(name = "id") Long id,
            Model model
    ) {
        AddArticleDTO article = articleService.findArticleById(id);
        if (article != null) {
            model.addAttribute("article", article);
            return "article_edit";
        } else {
            WebExceptionHandler.ResponseError error = new WebExceptionHandler.ResponseError();
            error.setMessage("Article not found");
            model.addAttribute("error", error);
            return "error";
        }
    }

    @PostMapping("{id}/edit")
    public String saveEditedArticle(
            @PathVariable(name = "id") Long id,
            @Valid @ModelAttribute AddArticleDTO article,
            BindingResult errors,
            RedirectAttributes redirectAttributes
    ) {
        if (errors.hasErrors()) {
            return "article_edit";
        } else {
            ExecutionResult result = articleService.saveChanges(article);
            if (result.getResultType().equals(EXECUTED_SUCCESSFULLY)) {
                redirectAttributes.addFlashAttribute(
                        SUCCESS_MESSAGE, "Changes saved successfully");
                return "redirect:/articles/" + id;
            } else {
                WebExceptionHandler.ResponseError error = new WebExceptionHandler.ResponseError();
                error.setMessage(result.getErrorMessage());
                return "error";
            }
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteArticle(
            @PathVariable(name = "id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        ExecutionResult result = articleService.deleteArticleById(id);
        if (result.getResultType().equals(EXECUTED_SUCCESSFULLY)) {
            redirectAttributes.addFlashAttribute(
                    SUCCESS_MESSAGE, "Article deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute(
                    FAILURE_MESSAGE, result.getErrorMessage());
        }
        return "redirect:/articles";
    }

    @GetMapping("/{id}/comments")
    public String showAddCommentForm(
            @PathVariable(name = "id") Long articleID,
            Model model
    ) {
        AddCommentDTO comment = new AddCommentDTO();
        comment.setArticleId(articleID);
        model.addAttribute("comment", comment);
        return "article_add_comment";
    }

    @PostMapping("/{id}/comments")
    public String addComment(
            @AuthenticationPrincipal AppUser appUser,
            @Valid @ModelAttribute(name = "comment") AddCommentDTO comment,
            BindingResult errors,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (errors.hasErrors()) {
            return "article_add_comment";
        } else {
            Long userId = appUser.getId();
            comment.setUserId(userId);
            ExecutionResult result = articleService.addComment(comment);
            if (result.getResultType().equals(EXECUTED_SUCCESSFULLY)) {
                redirectAttributes.addFlashAttribute(
                        SUCCESS_MESSAGE, "Comment added successfully");
                return "redirect:/articles/" + comment.getArticleId();
            } else {
                WebExceptionHandler.ResponseError error = new WebExceptionHandler.ResponseError();
                error.setMessage(result.getErrorMessage());
                model.addAttribute("error", error);
                return "error";
            }
        }
    }

    @GetMapping("/{articleId}/comments/{commentId}/delete")
    public String deleteComment(
            @PathVariable(name = "articleId") Long articleId,
            @PathVariable(name = "commentId") Long commentId,
            RedirectAttributes redirectAttributes
    ) {
        ExecutionResult result = articleService.deleteComment(commentId);
        if (result.getResultType().equals(EXECUTED_SUCCESSFULLY)) {
            redirectAttributes.addFlashAttribute(
                    SUCCESS_MESSAGE, "Comment deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute(
                    FAILURE_MESSAGE, result.getErrorMessage());
        }
        return "redirect:/articles/" + articleId;
    }

}