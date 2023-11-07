package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    // Create
    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto categoryDto) {

        CategoryDto categoryDto1 = categoryService.create(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    // update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String categoryId, @RequestBody CategoryDto categoryDto) {
        CategoryDto updated = categoryService.update(categoryDto, categoryId);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {
        categoryService.delete(categoryId);
        ApiResponseMessage response = ApiResponseMessage.builder().message("Category deleted successfully !!").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {

        PageableResponse<CategoryDto> userDtoList = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);

    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable String categoryId) {
        CategoryDto categoryDto = categoryService.get(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    // Create product with Category
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> productWithCategory(@PathVariable("categoryId") String categoryId, @RequestBody ProductDto productDto) {
        ProductDto productWithCategory = productService.createWithCategory(productDto, categoryId);
        return new ResponseEntity<>(productWithCategory, HttpStatus.CREATED);
    }

    // Update Category of Product
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateProductOfCategory(
            @PathVariable("categoryId") String categoryId,
            @PathVariable("productId") String productId
    ){
        ProductDto productDto = productService.updateCategory(productId, categoryId);
        return  new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductOfCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
            ){
        PageableResponse<ProductDto> allOfCategories = productService.getAllOfCategories(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return  new ResponseEntity<>(allOfCategories, HttpStatus.OK);
    }
}
