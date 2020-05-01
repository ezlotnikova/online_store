package com.gmail.ezlotnikova.web.controller;

import java.util.List;
import javax.validation.Valid;

import com.gmail.ezlotnikova.service.ArticleService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.AddArticleDTO;
import com.gmail.ezlotnikova.service.model.ArticlePreviewDTO;
import com.gmail.ezlotnikova.service.model.ArticleWithCommentsDTO;
import com.gmail.ezlotnikova.service.model.CommentDTO;
import com.gmail.ezlotnikova.service.model.UpdateArticleDTO;
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
        UpdateArticleDTO updatedArticle = new UpdateArticleDTO();
        model.addAttribute("article", article);
        model.addAttribute("updatedArticle", updatedArticle);
        List<CommentDTO> comments = article.getComments();
        model.addAttribute("comments", comments);
        return "article_with_comments";
    }

    //    @PostMapping("/{id}")
    //    public String editArticle(
    //            @PathVariable(name = "id") Long id,
    //            @Valid @ModelAttribute(name = "updatedArticle") UpdateArticleDTO updatedArticle,
    //            BindingResult errors,
    //            RedirectAttributes redirectAttributes
    //    ) {
    //        if (errors.hasErrors()) {
    //            return "article_with_comments";
    //        } else {
    //            System.out.println(updatedArticle.getNewHeader());
    ////            articleService.save(updatedArticle);
    //            redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Article updated successfully");
    //            return "redirect:/articles/" + id;
    //        }
    //    }

    @GetMapping("/{id}/delete")
    public String deleteArticle(
            @PathVariable(name = "id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        ExecutionResult result = articleService.deleteArticleById(id);
        if (result.getResultType() == EXECUTED_SUCCESSFULLY) {
            redirectAttributes.addFlashAttribute(
                    SUCCESS_MESSAGE, "Article deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute(
                    FAILURE_MESSAGE, result.getErrorMessage());
        }
        return "redirect:/articles";
    }

    @GetMapping("/{articleId}/comments/{commentId}/delete")
    public String deleteComment(
            @PathVariable(name = "articleId") Long articleId,
            @PathVariable(name = "commentId") Long commentId,
            RedirectAttributes redirectAttributes
    ) {
        ExecutionResult result = articleService.deleteComment(commentId);
        if (result.getResultType() == EXECUTED_SUCCESSFULLY) {
            redirectAttributes.addFlashAttribute(
                    SUCCESS_MESSAGE, "Comment deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute(
                    FAILURE_MESSAGE, result.getErrorMessage());
        }
        return "redirect:/articles/" + articleId;
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

}