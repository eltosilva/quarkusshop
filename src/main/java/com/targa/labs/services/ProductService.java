package com.targa.labs.services;

import com.targa.labs.models.Product;
import com.targa.labs.models.ProductStatus;
import com.targa.labs.repositories.CategoryRepository;
import com.targa.labs.repositories.ProductRepository;
import com.targa.labs.services.dtos.ProductDto;
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
public class ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public List<ProductDto> findAll(){
        log.debug("Request to get all Products");

        return productRepository.findAll()
                .stream()
                .map(ProductDto::mapToDto)
                .collect(Collectors.toList());
    }

    public ProductDto findById(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id)
                .map(ProductDto::mapToDto)
                .orElse(null);
    }

    public Long countAll(){
        return productRepository.count();
    }

    public Long countByCategoryId(Long categoryId) {
        return productRepository.countAllByCategoryId(categoryId);
    }

    public ProductDto create(ProductDto productDto) {
        log.debug("Request to create Product : {}", productDto);

        Product product = new Product(
                productDto.getName(),
                productDto.getDescription(),
                productDto.getPrice(),
                ProductStatus.valueOf(productDto.getStatus()),
                productDto.getSalesCounter(),
                Collections.emptySet(),
                categoryRepository.findById(productDto.getCategoryId()).orElse(null)
                );

        return ProductDto.mapToDto(productRepository.save(product));
    }

    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }

    public List<ProductDto> findByCategoryId(Long categoryId) {
        return productRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(ProductDto::mapToDto)
                .collect(Collectors.toList());
    }
}
