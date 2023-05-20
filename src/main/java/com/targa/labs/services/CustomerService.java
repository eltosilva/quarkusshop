package com.targa.labs.services;

import com.targa.labs.models.Customer;
import com.targa.labs.repositories.CustomerRepository;
import com.targa.labs.services.dtos.CustomerDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@Transactional
@AllArgsConstructor
public class CustomerService {

    private CustomerRepository customerRepository;

    public CustomerDto create(CustomerDto customerDto){
        log.debug("Request to create Customer : {}", customerDto);

        Customer customer = new Customer(customerDto.getFirstName(), customerDto.getLastName(),
                customerDto.getEmail(), customerDto.getTelephone(), Collections.emptySet(), Boolean.TRUE);
        return CustomerDto.mapToDto(customerRepository.save(customer));
    }

    public List<CustomerDto> findAll(){
        log.debug("Request to get all Customers");
        return customerRepository.findAll()
                .stream()
                .map(CustomerDto::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomerDto findById(Long id){
        log.debug("Request to get Customer : {}", id);
        return customerRepository.findById(id)
                .map(CustomerDto::mapToDto)
                .orElse(null);
    }

    public List<CustomerDto> findAllActive(){
        log.debug("Request to get all active customers");
        return customerRepository.findAllByEnabled(Boolean.TRUE)
                .stream()
                .map(CustomerDto::mapToDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id){
        log.debug("Request to delete Customer : {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find Customer with id " + id));
        customer.setEnabled(Boolean.FALSE);
        customerRepository.save(customer);
    }
}
