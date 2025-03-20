package com.golu.electronic.store.repositories;

import com.golu.electronic.store.entities.Category;
import com.golu.electronic.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

    //search
    Page<Product> findByTitleContaining(Pageable pageable, String title);

    Page<Product> findByLiveTrue(Pageable pageable);

    Page<Product> findByCategory(Pageable pageable, Category category);
}
