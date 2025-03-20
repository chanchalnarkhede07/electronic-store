package com.golu.electronic.store.controllers;

import com.golu.electronic.store.dtos.ApiResponseMessage;
import com.golu.electronic.store.dtos.CategoryDto;
import com.golu.electronic.store.dtos.ImageResponse;
import com.golu.electronic.store.dtos.PageableResponse;
import com.golu.electronic.store.services.CategoryService;
import com.golu.electronic.store.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Value("${category.image.path}")
    private String categoryFileUploadPath;


    //create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto categoryDto1 = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String categoryId,
                                                      @Valid @RequestBody CategoryDto categoryDto
    ) {
        CategoryDto updatedCategoryDto = categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(updatedCategoryDto, HttpStatus.OK);
    }


    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategoryById(categoryId);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .httpStatus(HttpStatus.OK)
                .message("Category is Deleted successfully ")
                .success(true)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    //get by id
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String categoryId) {
        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<CategoryDto> response = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(
            @RequestParam("CategoryImage") MultipartFile image,
            @PathVariable String categoryId
    ) throws IOException {
        String imageName = fileService.uploadFile(image, categoryFileUploadPath);
        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
        categoryDto.setCoverImage(imageName);
        categoryService.updateCategory(categoryDto, categoryId);
        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .message("Image saved Successfully")
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    @GetMapping("/image/{categoryId}")
    public void getCategoryImage(
            @PathVariable String categoryId,
            HttpServletResponse httpResponse
    ) throws IOException {
        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
        InputStream resource = fileService.getFile(categoryFileUploadPath, categoryDto.getCoverImage());
        httpResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, httpResponse.getOutputStream());
    }


}
