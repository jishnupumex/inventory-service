package com.supply.inventoryservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;


@Getter
@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int prodId;
    private String prodName;
    private String prodDesc;
    private String prodType;
    private int prodPrice;
    private int prodQty;

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public void setProdPrice(int prodPrice) {
        this.prodPrice = prodPrice;
    }

    public void setProdQty(int prodQty) {
        this.prodQty = prodQty;
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

