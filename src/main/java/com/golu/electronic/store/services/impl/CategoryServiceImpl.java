package com.golu.electronic.store.services.impl;

import com.golu.electronic.store.dtos.CategoryDto;
import com.golu.electronic.store.dtos.PageableResponse;
import com.golu.electronic.store.entities.Category;
import com.golu.electronic.store.exceptions.ResourceNotFoundException;
import com.golu.electronic.store.helper.Helper;
import com.golu.electronic.store.repositories.CategoryRepository;
import com.golu.electronic.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${category.image.path}")
    private String categoryFileUploadPath;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        String catId= UUID.randomUUID().toString();
        categoryDto.setCategoryId(catId);
        Category category = categoryRepository.save(modelMapper.map(categoryDto, Category.class));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory = categoryRepository.save(modelMapper.map(category, Category.class));
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategoryById(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        //delete user image
        String fullPath = categoryFileUploadPath + category.getCoverImage();

        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategories(int pageNum, int pageSize, String sortBy, String order) {
        Sort sort = order.equalsIgnoreCase("desc") ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
        return pageableResponse;
    }

    @Override
    public CategoryDto getCategoryById(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        return categoryDto;
    }
}
