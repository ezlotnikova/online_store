package com.gmail.ezlotnikova.service.model;

import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.MAX_ITEM_DESCRIPTION_SIZE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationConstant.MAX_ITEM_NAME_SIZE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.NOT_EMPTY_MESSAGE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.PRICE_LESS_THEN_ZERO_MESSAGE;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.TOO_LONG_ITEM_DESCRIPTION;
import static com.gmail.ezlotnikova.service.model.validation.ModelValidationMessageConstant.TOO_LONG_ITEM_NAME;

public class ItemDTO {

    private Long id;

    private String uniqueNumber;

    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    @Size(max = MAX_ITEM_NAME_SIZE, message = TOO_LONG_ITEM_NAME)
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = PRICE_LESS_THEN_ZERO_MESSAGE)
    @Digits(integer = 8, fraction = 2)
    private BigDecimal price;

    @Size(max = MAX_ITEM_DESCRIPTION_SIZE, message = TOO_LONG_ITEM_DESCRIPTION)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemDTO itemDTO = (ItemDTO) o;
        return Objects.equals(id, itemDTO.id) &&
                Objects.equals(uniqueNumber, itemDTO.uniqueNumber) &&
                Objects.equals(name, itemDTO.name) &&
                Objects.equals(price, itemDTO.price) &&
                Objects.equals(description, itemDTO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uniqueNumber, name, price, description);
    }

}