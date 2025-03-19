package com.golu.electronic.store.services;

import com.golu.electronic.store.dtos.CategoryDto;
import com.golu.electronic.store.dtos.PageableResponse;

public interface CategoryService {

    //create
    CategoryDto createCategory(CategoryDto categoryDto);

    //update
    CategoryDto updateCategory(CategoryDto categoryDto, String catId);

    //delete
    void deleteCategoryById(String id);

    //get all category
    PageableResponse<CategoryDto> getAllCategories(int pageNum, int pageSize, String sortBy, String order);

    //get single category
    CategoryDto getCategoryById(String id);
}
