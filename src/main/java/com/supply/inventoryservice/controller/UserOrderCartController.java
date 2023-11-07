package com.supply.inventoryservice.controller;

import com.scm.UserOrder;
import com.supply.inventoryservice.entity.UserCart;
import com.supply.inventoryservice.service.UserOrderCartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user-cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserOrderCartController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserOrderCartController.class);
    @Autowired
    private final UserOrderCartService userOrderCartService;

    @PostMapping("/add")
    public ResponseEntity<UserCart> getProductDetails(@RequestBody UserOrder request) {
        try {
            UserCart productDetails = userOrderCartService.saveUserCart((long) request.getUserId(), (long) request.getProdId());
            return ResponseEntity.ok(productDetails);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get-cart")
    public List<UserCart> getUserCartByUserIdAndProdId(
            @RequestParam Long userId,
            @RequestParam Long prodId
    ) {
        return userOrderCartService.getUserCartByUserIdAndProdId(userId, prodId);
    }
    // Cart page product quantity update API
    // Parameters - UserID, ProdID, ProdQty
    @PutMapping("/update-prod")
    public UserCart updateUserCart(@RequestBody UserCart userCart) {
        return userOrderCartService.updateUserCart(userCart);
    }

    //    Fetching products added into cart for a specific USER using UserID
    //    Parameter - UserID
    @GetMapping("/user/{userId}")
    public List<UserCart> getUserCartByUserId(@PathVariable Long userId) {
        return userOrderCartService.getUserCartByUserId(userId);
    }
}
