package com.golu.electronic.store.repositories;

import com.golu.electronic.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    //search
    Page<Product> findByTitleContaining(Pageable pageable, String title);

    Page<Product> findByLiveTrue(Pageable pageable);
}
