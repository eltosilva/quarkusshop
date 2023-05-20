package com.targa.labs.services;

import com.targa.labs.models.Cart;
import com.targa.labs.models.CartStatus;
import com.targa.labs.models.Customer;
import com.targa.labs.repositories.CartRepository;
import com.targa.labs.repositories.CustomerRepository;
import com.targa.labs.services.dtos.CartDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@Transactional
@AllArgsConstructor
public class CartService {

    private CartRepository cartRepository;
    private CustomerRepository customerRepository;

    public List<CartDto> findAll(){
        log.debug("Request to get all Carts");
        return cartRepository.findAll()
                .stream()
                .map(CartDto::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CartDto> findAllActiveCarts(){
        return cartRepository.findByStatus(CartStatus.NEW)
                .stream()
                .map(CartDto::mapToDto)
                .collect(Collectors.toList());
    }

    public Cart create(Long customerId) {
        if(getActiveCart(customerId) == null) {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalStateException("The customer does not exist!"));
            Cart cart = new Cart(customer, CartStatus.NEW);

            return cartRepository.save(cart);
        }else{
            throw new IllegalStateException("There is already an active cart");
        }
    }

    public CartDto createDto(Long customerId) {
        return CartDto.mapToDto(create(customerId));
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public CartDto findById(Long id){
        log.debug("Request to get Cart: {}", id);

        return cartRepository.findById(id)
                .map(CartDto::mapToDto)
                .orElse(null);
    }

    public void delete(Long id){
        log.debug("Request to delete Cart: {}", id);

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find cart with id " + id));

        cart.setStatus(CartStatus.CANCELED);
        cartRepository.save(cart);
    }

    public CartDto getActiveCart(Long customerId) {
        List<Cart> carts = cartRepository.findByStatusAndCustomerId(CartStatus.NEW, customerId);

        if (carts != null) {
            if(carts.size() == 1)
                return CartDto.mapToDto(carts.get(0));
            if(carts.size() > 1)
                throw new IllegalStateException("Many active carts detected !!!");
        }

        return null;
    }

}
