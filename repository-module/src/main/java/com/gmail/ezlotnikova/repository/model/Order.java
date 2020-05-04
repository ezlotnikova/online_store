package com.gmail.ezlotnikova.repository.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gmail.ezlotnikova.repository.model.сonstant.OrderStatusEnum;

@Entity
@Table(name = "purchase")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private Integer amount;

    @Column
    private BigDecimal sum;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @ManyToOne(
            fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserDetails userDetails;

    @ManyToOne(
            fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(amount, order.amount) &&
                Objects.equals(sum, order.sum) &&
                Objects.equals(createdOn, order.createdOn) &&
                status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, sum, createdOn, status);
    }

}