package com.supply.inventoryservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int prodId;
    @Getter
    private String prodName;
    @Getter
    private String prodDesc;
    @Getter
    private String prodType;
    @Getter
    private int prodPrice;
    @Getter
    private int prodQty;


    public long getProdId() {
        return prodId;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "prodId=" + prodId +
                ", prodName='" + prodName + '\'' +
                ", prodDesc='" + prodDesc + '\'' +
                ", prodType='" + prodType + '\'' +
                ", prodPrice=" + prodPrice +
                ", prodQty=" + prodQty +
                '}';
    }
}

