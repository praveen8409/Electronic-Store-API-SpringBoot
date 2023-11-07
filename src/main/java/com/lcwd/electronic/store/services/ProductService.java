package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto create(ProductDto productDto);

    ProductDto update(ProductDto productDto, String productId);

    void delete(String productId);

    PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    ProductDto get(String productId);

    PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);

    PageableResponse<ProductDto> searchByTitle(String title, int pageNumber, int pageSize, String sortBy, String sortDir);

    // Create Product with Category
    ProductDto createWithCategory(ProductDto productDto, String categoryId);

    // Updae Category
    ProductDto updateCategory(String productId, String categoryId);

    // Get All Category products
    PageableResponse<ProductDto> getAllOfCategories(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);

}
