package com.gmail.ezlotnikova.web.controller;

import javax.validation.Valid;

import com.gmail.ezlotnikova.service.PasswordService;
import com.gmail.ezlotnikova.service.UserDetailsService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.PasswordDTO;
import com.gmail.ezlotnikova.service.model.UserDetailsDTO;
import com.gmail.ezlotnikova.service.security.AppUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.gmail.ezlotnikova.service.constant.ResultTypeEnum.EXECUTED_SUCCESSFULLY;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.FAILURE_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.SUCCESS_MESSAGE;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserDetailsService userDetailsService;
    private final PasswordService passwordService;

    public ProfileController(
            UserDetailsService userDetailsService,
            PasswordService passwordService) {
        this.userDetailsService = userDetailsService;
        this.passwordService = passwordService;
    }

    @GetMapping()
    public String showUserProfile(
            @AuthenticationPrincipal AppUser appUser,
            Model model
    ) {
        Long id = appUser.getId();
        UserDetailsDTO profile = userDetailsService.getUserDetailsById(id);
        model.addAttribute("profile", profile);
        return "profile";
    }

    @PostMapping()
    public String updateUserProfile(
            @AuthenticationPrincipal AppUser appUser,
            @Valid @ModelAttribute(name = "profile") UserDetailsDTO profile,
            BindingResult errors,
            RedirectAttributes redirectAttributes
    ) {
        Long id = appUser.getId();
        profile.setId(id);
        UserDetailsDTO databaseProfile = userDetailsService.getUserDetailsById(id);
        if (!errors.hasErrors()) {
            if (!profile.equals(databaseProfile)) {
                ExecutionResult result = userDetailsService.updateUserDetails(id, profile);
                if (result.getResultType() == EXECUTED_SUCCESSFULLY) {
                    redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Profile updated successfully");
                } else {
                    redirectAttributes.addFlashAttribute(FAILURE_MESSAGE,
                            "Something went wrong and changes wasn't saved. Please try again.");
                }
                return "redirect:/profile";
            }
        }
        return "profile";
    }

    @GetMapping("/password")
    public String showChangePasswordForm(Model model) {
        PasswordDTO password = new PasswordDTO();
        model.addAttribute("password", password);
        return "password_change";
    }

    @PostMapping("/password")
    public String changeUserPassword(
            @AuthenticationPrincipal AppUser appUser,
            @Valid @ModelAttribute(name = "password") PasswordDTO password,
            BindingResult errors,
            RedirectAttributes redirectAttributes
    ) {
        if (errors.hasErrors()) {
            return "password_change";
        } else {
            Long id = appUser.getId();
            ExecutionResult result = passwordService.changePasswordByUserId(id, password.getNewPassword());
            if (result.getResultType() == EXECUTED_SUCCESSFULLY) {
                redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Password changed successfully");
            } else {
                redirectAttributes.addFlashAttribute(FAILURE_MESSAGE,
                        "Something went wrong and password wasn't changed. Please try again.");
            }
            return "redirect:/profile";
        }
    }

}