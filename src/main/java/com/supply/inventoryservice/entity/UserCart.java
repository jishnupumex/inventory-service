package com.supply.inventoryservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_cart")
public class UserCart {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Long getUserOrderId() {
        return userOrderId;
    }

    public void setUserOrderId(Long userOrderId) {
        this.userOrderId = userOrderId;
    }

    public String getProdImage() {
        return prodImage;
    }

    public void setProdImage(String prodImage) {
        this.prodImage = prodImage;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public int getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(int prodPrice) {
        this.prodPrice = prodPrice;
    }

    public int getProdQty() {
        return prodQty;
    }

    public void setProdQty(int prodQty) {
        this.prodQty = prodQty;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

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
