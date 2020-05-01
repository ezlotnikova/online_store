package com.gmail.ezlotnikova.service.model;

import java.util.Objects;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(max = MAX_LAST_NAME_SIZE)
    private String lastName;

    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(max = MAX_FIRST_NAME_SIZE)
    private String firstName;

    @Size(max = MAX_PATRONYMIC_NAME_SIZE)
    private String patronymicName;

    @NotEmpty(message = NOT_EMPTY_MESSAGE)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddUserDTO userDTO = (AddUserDTO) o;
        return Objects.equals(id, userDTO.id) &&
                Objects.equals(lastName, userDTO.lastName) &&
                Objects.equals(firstName, userDTO.firstName) &&
                Objects.equals(patronymicName, userDTO.patronymicName) &&
                Objects.equals(email, userDTO.email) &&
                role == userDTO.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastName, firstName, patronymicName, email, role);
    }

}