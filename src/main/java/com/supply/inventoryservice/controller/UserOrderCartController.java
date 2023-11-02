package com.supply.inventoryservice.controller;

import com.supply.inventoryservice.entity.UserCart;
import com.supply.inventoryservice.service.UserOrderCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user-cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8000")
public class UserOrderCartController {
    @Autowired
    private final UserOrderCartService userOrderCartService;

    //    From home page adding to cart API         Parameters - UserID, ProdID
    @PostMapping("/add")
    public UserCart addUserCart(@RequestBody UserCart userCart) {
        return userOrderCartService.saveUserCart(userCart);
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
