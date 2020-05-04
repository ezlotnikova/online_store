package com.gmail.ezlotnikova.service.model;

import java.math.BigDecimal;

import com.gmail.ezlotnikova.repository.model.сonstant.OrderStatusEnum;

public class OrderPreviewDTO {

    private Long id;
    private OrderStatusEnum status;
    private String itemName;
    private Integer amount;
    private BigDecimal sum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

}