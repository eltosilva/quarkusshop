package com.targa.labs.services.dtos;

import com.targa.labs.models.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String name;
    private String description;
    private BigDecimal price;
    private String status;
    private Integer salesCounter;
    private Set<ReviewDto> reviews;
    private Long categoryId;

    public static ProductDto mapToDto(Product product) {

        return new ProductDto(
                product.getName(), product.getDescription(), product.getPrice(), product.getStatus().name(), product.getSalesCounter(),
                product.getReviews().stream().map(ReviewDto::mapToDto).collect(Collectors.toSet()), product.getCategory().getId()
                );
    }
}
