package com.gmail.ezlotnikova.repository.model;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "item")
@SQLDelete(sql =
        "UPDATE item " +
                "SET is_available = false " +
                "WHERE id = ?")
//@Where(clause = "is_available = true")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "unique_number")
    private String uniqueNumber;

    @Column
    private String name;

    @Column
    private BigDecimal price;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @OneToOne(
            mappedBy = "item",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL}
    )
    private ItemDetails itemDetails;

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

    public ItemDetails getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(ItemDetails itemDetails) {
        this.itemDetails = itemDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return Objects.equals(id, item.id) &&
                Objects.equals(uniqueNumber, item.uniqueNumber) &&
                Objects.equals(name, item.name) &&
                Objects.equals(price, item.price) &&
                Objects.equals(itemDetails, item.itemDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uniqueNumber, name, price, itemDetails);
    }

}