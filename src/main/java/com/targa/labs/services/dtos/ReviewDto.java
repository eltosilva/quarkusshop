package com.targa.labs.services.dtos;

import com.targa.labs.models.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;
    private String title;
    private String description;
    private Long rating;

    public static ReviewDto mapToDto(Review review) {
        return new ReviewDto(review.getId(), review.getTitle(), review.getDescription(), review.getRating());
    }
}
