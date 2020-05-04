package com.gmail.ezlotnikova.web.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {

    @GetMapping("/forbidden")
    public String showAccessDenied(){
        return "access_denied";
    }

}
