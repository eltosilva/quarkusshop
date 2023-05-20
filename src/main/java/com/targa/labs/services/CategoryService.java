package com.targa.labs.services;

import com.targa.labs.models.Category;
import com.targa.labs.repositories.CategoryRepository;
import com.targa.labs.repositories.ProductRepository;
import com.targa.labs.services.dtos.CategoryDto;
import com.targa.labs.services.dtos.ProductDto;
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
public class CategoryService {

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    public List<CategoryDto> findAll(){
        log.debug("Request to get all Categories");

        return categoryRepository.findAll()
                .stream()
                .map(category -> mapToDto(
                        category,
                        productRepository.countAllByCategoryId(category.getId()))
                )
                .collect(Collectors.toList());
    }

    public CategoryDto findByid(Long id) {
        log.debug("Request to get Category: {}", id);
        return categoryRepository.findById(id)
                .map(category -> mapToDto(category, productRepository.countAllByCategoryId(category.getId())))
                .orElse(null);
    }

    public CategoryDto create(CategoryDto categoryDto){
        log.debug("Request to create Category: {}", categoryDto);

        Category category = new Category(categoryDto.getName(), categoryDto.getDescription());
        return mapToDto(categoryRepository.save(category), 0l);
    }

    public void delete(Long id){
        log.debug("Request to delete Category: {}", id);

        log.debug("Deleting all products for the Category : {}", id);
        productRepository.deleteAllByCategoryId(id);

        log.debug("Deleting Category : {}", id);
        categoryRepository.deleteById(id);
    }

    public static CategoryDto mapToDto(Category category, Long productsCount){
        return new CategoryDto(category.getId(), category.getName(), category.getDescription(), productsCount);
    }

    public List<ProductDto> findProductsByCategoryId(Long id){
        return productRepository.findAllByCategoryId(id)
                .stream()
                .map(ProductDto::mapToDto)
                .collect(Collectors.toList());
    }
}
