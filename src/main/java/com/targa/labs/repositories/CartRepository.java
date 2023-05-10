package com.targa.labs.repositories;

import com.targa.labs.models.Cart;
import com.targa.labs.models.CartStatus;
import com.targa.labs.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByStatus(CartStatus status);

    List<Cart> findByStatusAndCustomerId(CartStatus status, Customer customer);
}
