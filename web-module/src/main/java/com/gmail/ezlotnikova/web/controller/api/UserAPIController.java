package com.gmail.ezlotnikova.web.controller.api;

import javax.validation.Valid;

import com.gmail.ezlotnikova.service.UserService;
import com.gmail.ezlotnikova.service.model.AddUserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserAPIController {

    private final UserService userService;

    public UserAPIController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddUserDTO addUser(
            @Valid @RequestBody AddUserDTO userDTO
    ) {
        return userService.add(userDTO);
    }

}