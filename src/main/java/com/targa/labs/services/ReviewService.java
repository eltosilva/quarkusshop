package com.targa.labs.services;

import com.targa.labs.models.Product;
import com.targa.labs.models.Review;
import com.targa.labs.repositories.ProductRepository;
import com.targa.labs.repositories.ReviewRepository;
import com.targa.labs.services.dtos.ReviewDto;
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
public class ReviewService {

    private ReviewRepository reviewRepository;
    private ProductRepository productRepository;

    public List<ReviewDto> findReviewsByProductId(Long id){
        log.debug("Request to get all Reviews");

        return reviewRepository.findReviewsByProductId(id)
                .stream()
                .map(ReviewDto::mapToDto)
                .collect(Collectors.toList());
    }

    public ReviewDto findById(Long id) {
        log.debug("Request to get Review : {}", id);

        return reviewRepository.findById(id)
                .map(ReviewDto::mapToDto)
                .orElse(null);
    }

    public ReviewDto create(ReviewDto reviewDto, Long productId) {
        log.debug("Request to create Review : {} ofr the Product {}", reviewDto, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Product with ID:" + productId + " was not found !"));

        Review savedReview = reviewRepository.saveAndFlush(new Review(
                reviewDto.getTitle(), reviewDto.getDescription(), reviewDto.getRating()
        ));

        product.getReviews().add(savedReview);
        productRepository.saveAndFlush(product);

        return ReviewDto.mapToDto(savedReview);
    }

    public void delete(Long id) {
        log.debug("Request to delete Review : {}", id);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Review with ID:" + id + " was not found !"));

        Product product = productRepository.findProductByReviewId(id)
                .orElseThrow(() -> new IllegalStateException("Product with Review ID:" + id + " was not found !"));
        product.getReviews().remove(review);
        productRepository.saveAndFlush(product);
        reviewRepository.delete(review);
    }
}
