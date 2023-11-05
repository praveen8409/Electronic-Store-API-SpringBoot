package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundExceptions;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        // Creating CategoryId : Randomly
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);

        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        CategoryDto SavedcategoryDto = mapper.map(savedCategory, CategoryDto.class);
        return SavedcategoryDto;
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        // Get the category with the given categoryId
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundExceptions("Category not found with given Id"));

        // Update the category
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());

        CategoryDto upadatedCategoryDto = mapper.map(category, CategoryDto.class);
        return upadatedCategoryDto;
    }

    @Override
    public void delete(String categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundExceptions("Category not found with given Id"));
        categoryRepository.deleteById(categoryId);

    }


    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        List<Category> userList = page.getContent();
        PageableResponse<CategoryDto> response = Helper.getPageableResponse(page, CategoryDto.class);
        return response;
    }

    @Override
    public CategoryDto get(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundExceptions("Category not found with given Id"));

        return mapper.map(category, CategoryDto.class);
    }
}
