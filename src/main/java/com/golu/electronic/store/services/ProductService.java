package com.golu.electronic.store.services;

import com.golu.electronic.store.dtos.PageableResponse;
import com.golu.electronic.store.dtos.ProductDto;

import java.util.List;

public interface ProductService {

    //create
    ProductDto createProduct(ProductDto productDto);

    //update
    ProductDto updateProduct(ProductDto productDto, String productId);

    //delete
    void deleteProduct(String productId);

    //get all
    PageableResponse<ProductDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortOrder);

    //get by id
    ProductDto getProduct(String productId);

    //search by title
    PageableResponse<ProductDto> findByTitleContaining(String productTitle, int pageNo, int pageSize, String sortBy, String sortOrder);

    //get live
    PageableResponse<ProductDto> findByLiveTrue(int pageNo, int pageSize, String sortBy, String sortOrder);
}
