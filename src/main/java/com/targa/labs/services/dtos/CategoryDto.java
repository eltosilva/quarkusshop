package com.targa.labs.services.dtos;

import com.targa.labs.models.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private Long products;

    public static CategoryDto mapToDto(Category category, Long productCount) {
        return new CategoryDto(category.getId(), category.getName(), category.getDescription(), productCount);
    }
}
