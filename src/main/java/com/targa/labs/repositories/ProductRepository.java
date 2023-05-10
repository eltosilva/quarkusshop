package com.targa.labs.repositories;

import com.targa.labs.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long id);
    Long countAllByCategoryId(Long id);
    @Query("select p from Product p join p.previews r where r.id = ?1")
    Optional<Product> findProductByReviewId(Long id);
    void deleteAllByCategoryId(Long id);
    List<Product> findAllByCategoryId(Long id);
}
