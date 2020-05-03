package com.gmail.ezlotnikova.web.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/forbidden")
public class AccessDeniedAPIController {

    @GetMapping
    public Object showAccessDeniedMessage(
    ) {
        return new AccessDeniedMessage();
    }

    private static class AccessDeniedMessage {

        private final String message = "Access denied";

    }

}