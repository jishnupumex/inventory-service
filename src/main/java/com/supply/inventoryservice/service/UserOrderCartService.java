package com.supply.inventoryservice.service;

import com.scm.UserOrder;
import com.supply.inventoryservice.entity.Inventory;
import com.supply.inventoryservice.entity.UserCart;
import com.supply.inventoryservice.repo.UserCartRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOrderCartService {
    private final UserCartRepository userCartRepository;
    private final InventoryService inventoryService;


    @Autowired
    public UserOrderCartService(UserCartRepository userCartRepository, InventoryService inventoryService) {
        this.userCartRepository = userCartRepository;
        this.inventoryService = inventoryService;
    }

    public UserCart saveUserCart(Long userId, Long prodId) {
        // Retrieve the product from the Inventory table based on prodID
        Inventory product = inventoryService.getInventoryByProdId(prodId);

        // Create a new UserCart entry with the product details
        UserCart userCart = new UserCart();
        userCart.setUserId(userId);
        userCart.setProdId(product.getProdId());
        userCart.setProdName(product.getProdName());
        userCart.setProdDesc(product.getProdDesc());
        userCart.setProdQty(1);
        userCart.setProdType(product.getProdType());
        userCart.setProdPrice(product.getProdPrice());
        userCart.setTotalPrice(userCart.getProdQty()*userCart.getProdPrice());
        // Set other fields as needed

        // Save the userCart entry
        return userCartRepository.save(userCart);
    }

    public UserOrder getOrdersByUserId(Long userId) {
        List<UserCart> orders = userCartRepository.findByUserId(userId);
        UserOrder response = new UserOrder();
//        response.setOrders(orders);
        return response;
    }

    public List<UserCart> getUserCartByUserIdAndProdId(Long userId, Long prodId) {
        return (List<UserCart>) userCartRepository.findByUserIdAndProdId(userId, prodId);
    }
    public UserCart updateUserCart(UserCart userCart) {
        // Retrieve the existing UserCart entry by userId and prodId
        UserCart existingUserCart = userCartRepository.findByUserIdAndProdId(userCart.getUserId(), userCart.getProdId());

        if (existingUserCart != null) {
            // Update the prodQty with the new value
            existingUserCart.setProdQty(userCart.getProdQty());

            // Update the prodDesc with the new value
            existingUserCart.setProdDesc(userCart.getProdDesc());
            existingUserCart.setProdName(userCart.getProdName());
            existingUserCart.setProdPrice(userCart.getProdPrice());
            existingUserCart.setProdId(userCart.getProdId());
            existingUserCart.setProdImage(userCart.getProdImage());


            // Calculate and update the totalPrice based on the new prodQty and prodPrice
            existingUserCart.setTotalPrice(existingUserCart.getProdPrice() * existingUserCart.getProdQty());

            // Save the updated UserCart entry
            return userCartRepository.save(existingUserCart);
        } else {
            throw new EntityNotFoundException("UserCart entry not found for userId: " + userCart.getUserId() + " and prodId: " + userCart.getProdId());
        }
    }
    public List<UserCart> getUserCartByUserId(Long userId) {
        return userCartRepository.findByUserId(userId);
    }

}
