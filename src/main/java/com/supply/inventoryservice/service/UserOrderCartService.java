package com.supply.inventoryservice.service;

import com.supply.inventoryservice.entity.UserCart;
import com.supply.inventoryservice.repo.UserCartRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOrderCartService {
    private final UserCartRepository userCartRepository;

    @Autowired
    public UserOrderCartService(UserCartRepository userCartRepository) {
        this.userCartRepository = userCartRepository;
    }

    public UserCart saveUserCart(UserCart userCart) {
        return userCartRepository.save(userCart);
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
            // Handle the case where the UserCart entry doesn't exist
            // You can throw an exception or handle it as per your application's requirements.
            // For example, you can return an error response.
            throw new EntityNotFoundException("UserCart entry not found for userId: " + userCart.getUserId() + " and prodId: " + userCart.getProdId());
        }
    }
    public List<UserCart> getUserCartByUserId(Long userId) {
        return userCartRepository.findByUserId(userId);
    }
}
