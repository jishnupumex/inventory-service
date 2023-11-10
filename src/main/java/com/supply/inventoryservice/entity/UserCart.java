package com.supply.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_cart")
public class UserCart {
    @Id
    private Long prodId;
    private Long userId;
    private Long userOrderId;
    private String prodName;
    private String prodDesc;
    private String prodType;
    private int prodPrice;
    private int prodQty;
    private String prodImage;
    private int totalPrice;


    @Override
    public String toString() {
        return "UserCart{" +
                "prodId=" + prodId +
                ", userId=" + userId +
                ", userOrderId=" + userOrderId +
                ", prodName='" + prodName + '\'' +
                ", prodDesc='" + prodDesc + '\'' +
                ", prodType='" + prodType + '\'' +
                ", prodPrice=" + prodPrice +
                ", prodQty=" + prodQty +
                ", prodImage='" + prodImage + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
