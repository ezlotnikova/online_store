package com.gmail.ezlotnikova.web.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.validation.Valid;

import com.gmail.ezlotnikova.repository.model.сonstant.UserRoleEnum;
import com.gmail.ezlotnikova.service.UserService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.AddUserDTO;
import com.gmail.ezlotnikova.service.model.ShowUserDTO;
import com.gmail.ezlotnikova.web.controller.constant.PaginationConstant;
import org.springframework.data.domain.Page;
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
import static com.gmail.ezlotnikova.service.constant.ResultTypeEnum.EXECUTION_FAILED;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String showUserByPage(
            Model model,
            @RequestParam("page") Optional<Integer> page
    ) {
        int currentPage = page.orElse(1);
        int pageSize = PaginationConstant.USERS_BY_PAGE;
        Page<ShowUserDTO> users = userService.findPaginatedAndOrderedByEmail(currentPage, pageSize);
        model.addAttribute("users", users);
        int totalPages = users.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "users";
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        AddUserDTO user = new AddUserDTO();
        model.addAttribute("user", user);
        return "user_add";
    }

    @PostMapping("/add")
    public String addNewUser(
            @Valid @ModelAttribute(name = "user") AddUserDTO user,
            BindingResult errors) {
        if (errors.hasErrors()) {
            return "user_add";
        } else {
            userService.add(user);
            return "redirect:/users";
        }
    }

    @GetMapping("/{id}/update-role")
    public String showUpdateRoleForm(
            Model model,
            @PathVariable(name = "id") String idString) {
        Long id = Long.parseLong(idString);
        ShowUserDTO user = userService.findUserById(id);
        model.addAttribute("user", user);
        List<UserRoleEnum> roles = Arrays.asList(UserRoleEnum.values());
        model.addAttribute("roles", roles);
        return "user_change_role";
    }

    @PostMapping("/{id}/update-role")
    public String showUpdateRoleForm(
            @PathVariable(name = "id") String idString,
            @RequestParam(value = "newRole") String newRole,
            RedirectAttributes redirectAttributes
    ) {
        Long id = Long.parseLong(idString);
        UserRoleEnum newRoleEnum = UserRoleEnum.valueOf(newRole);
        ExecutionResult result = userService.updateUserRoleById(id, newRoleEnum);
        if (result.getResultType().equals(EXECUTION_FAILED)) {
            redirectAttributes.addFlashAttribute(
                    "failureMessage", result.getErrorMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/{id}/change-password")
    public String changeUserPassword(
            @PathVariable(name = "id") String idString,
            RedirectAttributes redirectAttributes
    ) {
        Long id = Long.parseLong(idString);
        ExecutionResult result = userService.changeUserPasswordByIdAndSendEmail(id);
        if (result.getResultType().equals(EXECUTED_SUCCESSFULLY)) {
            redirectAttributes.addFlashAttribute(
                    "successMessage", "New password was sent to user's email");
        } else {
            redirectAttributes.addFlashAttribute(
                    "failureMessage", result.getErrorMessage());
        }
        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String deleteSelectedUsers(
            @RequestParam(name = "idList", required = false) List<String> idList,
            RedirectAttributes redirectAttributes
    ) {
        if (idList != null) {
            boolean allUsersDeleted = true;
            StringBuilder errorMessage = new StringBuilder();
            for (String idString : idList) {
                Long id = Long.parseLong(idString);
                ExecutionResult result = userService.deleteById(id);
                if (result.getResultType().equals(EXECUTION_FAILED)) {
                    allUsersDeleted = false;
                    errorMessage.append(result.getErrorMessage());
                }
            }
            if (!allUsersDeleted) {
                redirectAttributes.addFlashAttribute(
                        "failureMessage", errorMessage.toString());
            }
        }
        return "redirect:/users";
    }

}