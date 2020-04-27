package com.gmail.ezlotnikova.web.controller;

import java.util.List;
import javax.validation.Valid;

import com.gmail.ezlotnikova.repository.model.сonstant.UserRoleEnum;
import com.gmail.ezlotnikova.service.UserService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.AddUserDTO;
import com.gmail.ezlotnikova.service.model.ShowUserDTO;
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
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.FAILURE_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.SUCCESS_MESSAGE;

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
            @RequestParam(value = "page", defaultValue = "1") int pageNumber
    ) {
        Page<ShowUserDTO> users = userService.findPaginatedAndOrderedByEmail(pageNumber);
        model.addAttribute("users", users);
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
            BindingResult errors,
            RedirectAttributes redirectAttributes
    ) {
        if (errors.hasErrors()) {
            return "user_add";
        } else {
            userService.add(user);
            redirectAttributes.addFlashAttribute(
                    SUCCESS_MESSAGE, "User was added successfully");
            return "redirect:/users";
        }
    }

    @GetMapping("/{id}")
    public String showUpdateRoleForm(
            Model model,
            @PathVariable(name = "id") Long id
    ) {
        ShowUserDTO user = userService.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", UserRoleEnum.values());
        return "user_change_role";
    }

    @PostMapping("/{id}")
    public String updateUserRole(
            @PathVariable(name = "id") Long id,
            @RequestParam(value = "newRole") UserRoleEnum newRole,
            RedirectAttributes redirectAttributes
    ) {
        ExecutionResult result = userService.updateUserRoleById(id, newRole);
        if (result.getResultType() == EXECUTION_FAILED) {
            redirectAttributes.addFlashAttribute(
                    FAILURE_MESSAGE, result.getErrorMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/{id}/change-password")
    public String changeUserPassword(
            @PathVariable(name = "id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        ExecutionResult result = userService.generatePasswordAndSendEmail(id);
        if (result.getResultType() == EXECUTED_SUCCESSFULLY) {
            redirectAttributes.addFlashAttribute(
                    SUCCESS_MESSAGE, "New password was sent to user's email");
        } else {
            redirectAttributes.addFlashAttribute(
                    FAILURE_MESSAGE, result.getErrorMessage());
        }
        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String deleteSelectedUsers(
            @RequestParam(name = "idList", required = false) List<Long> idList,
            RedirectAttributes redirectAttributes
    ) {
        if (idList != null) {
            boolean allUsersDeleted = true;
            StringBuilder errorMessage = new StringBuilder();
            for (Long id : idList) {
                ExecutionResult result = userService.deleteById(id);
                if (result.getResultType() == EXECUTION_FAILED) {
                    allUsersDeleted = false;
                    errorMessage.append(result.getErrorMessage()).append(" ");
                }
            }
            if (!allUsersDeleted) {
                redirectAttributes.addFlashAttribute(
                        FAILURE_MESSAGE, errorMessage.toString());
            }
        }
        return "redirect:/users";
    }

}