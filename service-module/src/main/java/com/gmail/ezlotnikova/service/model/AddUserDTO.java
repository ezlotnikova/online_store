package com.gmail.ezlotnikova.service.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.gmail.ezlotnikova.repository.model.сonstant.UserRoleEnum;

import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.EMAIL_ADDRESS_PATTERN;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.MAX_FIRST_NAME_SIZE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.MAX_LAST_NAME_SIZE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.MAX_PATRONYMIC_NAME_SIZE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.INVALID_EMAIL_ADDRESS_MESSAGE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.NOT_EMPTY_MESSAGE;

public class AddUserDTO {

    private Long id;
    @NotNull
    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(max = MAX_LAST_NAME_SIZE)
    private String lastName;
    @NotNull
    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(max = MAX_FIRST_NAME_SIZE)
    private String firstName;
    @Size(max = MAX_PATRONYMIC_NAME_SIZE)
    private String patronymicName;
    @NotNull
//    @Pattern(regexp = EMAIL_ADDRESS_PATTERN, message = INVALID_EMAIL_ADDRESS_MESSAGE)
    @Email(regexp = EMAIL_ADDRESS_PATTERN, message = INVALID_EMAIL_ADDRESS_MESSAGE)
    private String email;
    @NotNull(message = NOT_EMPTY_MESSAGE)
    private UserRoleEnum role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymicName() {
        return patronymicName;
    }

    public void setPatronymicName(String patronymicName) {
        this.patronymicName = patronymicName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }

}