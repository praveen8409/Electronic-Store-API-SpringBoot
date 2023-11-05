package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;

import java.util.List;

public interface CategoryService {

    // Create
    CategoryDto create(CategoryDto categoryDto);

    // Update
    CategoryDto update(CategoryDto categoryDto, String categoryId);

    // Delete

    void delete(String categoryId);

    // Get All
    PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    // Get Category by categoryId
    CategoryDto get(String categoryId);
}
