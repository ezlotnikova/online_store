package com.gmail.ezlotnikova.service.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.gmail.ezlotnikova.service.validation.PasswordMatches;

import static com.gmail.ezlotnikova.service.model.constant.ValidationMessageConstant.NOT_EMPTY_MESSAGE;

@PasswordMatches
public class PasswordDTO {

    @NotNull
    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    private String newPassword;
    private String matchingPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

}