package com.gmail.ezlotnikova.web.controller;

import com.gmail.ezlotnikova.service.model.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping()
    public String showLoginForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "login";
    }

}