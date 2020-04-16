package com.gmail.ezlotnikova.service.model;

import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.gmail.ezlotnikova.service.model.constant.ValidationConstant.MAX_ADDRESS_SIZE;
import static com.gmail.ezlotnikova.service.model.constant.ValidationConstant.MAX_FIRST_NAME_SIZE;
import static com.gmail.ezlotnikova.service.model.constant.ValidationConstant.MAX_LAST_NAME_SIZE;
import static com.gmail.ezlotnikova.service.model.constant.ValidationConstant.PHONE_NUMBER_PATTERN;
import static com.gmail.ezlotnikova.service.model.constant.ValidationMessageConstant.INVALID_PHONE_NUMBER_MESSAGE;
import static com.gmail.ezlotnikova.service.model.constant.ValidationMessageConstant.NOT_EMPTY_MESSAGE;
import static com.gmail.ezlotnikova.service.model.constant.ValidationMessageConstant.TOO_LONG_ADDRESS_MESSAGE;

public class UserDetailsDTO {

    private Long id;
    @NotNull
    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(max = MAX_FIRST_NAME_SIZE)
    private String firstName;
    @NotNull
    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(max = MAX_LAST_NAME_SIZE)
    private String lastName;
    @Size(max = MAX_ADDRESS_SIZE, message = TOO_LONG_ADDRESS_MESSAGE)
    private String address;
    @Pattern(regexp = PHONE_NUMBER_PATTERN, message = INVALID_PHONE_NUMBER_MESSAGE)
    private String telephone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDetailsDTO that = (UserDetailsDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(address, that.address) &&
                Objects.equals(telephone, that.telephone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, address, telephone);
    }

}