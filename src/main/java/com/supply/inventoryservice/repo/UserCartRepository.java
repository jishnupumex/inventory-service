package com.supply.inventoryservice.repo;

import com.supply.inventoryservice.entity.UserCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserCartRepository extends JpaRepository<UserCart, Long> {
    List<UserCart> findByUserId(Long userId);
    UserCart findByUserIdAndProdId(Long userId, Long prodId);

}