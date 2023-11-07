package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundExceptions;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.services.ProductService;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${product.image.path}")
    private String imagePath;


    @Override
    public ProductDto create(ProductDto productDto) {

        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        Product product = mapper.map(productDto, Product.class);
        Product saveProduct = productRepository.save(product);
        return mapper.map(saveProduct, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundExceptions("Product not found with given id: " + productId));

        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImageName(productDto.getProductImageName());
        productRepository.save(product);

        return mapper.map(product, ProductDto.class);
    }

    @Override
    public void delete(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundExceptions("Product not found with given id: " + productId));

        //delete product image
        //images/products/abc.png
        String fullPath = imagePath + product.getProductImageName();

        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
//            logger.info("User image not found in folder");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        productRepository.delete(product);
    }


    @Override
    public ProductDto get(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundExceptions("Product not found with given id: " + productId));
        return mapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Product> page = productRepository.findAll(pageable);
        List<Product> productList = page.getContent();

        PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
        return response;
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);
        List<Product> productList = page.getContent();

        PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
        return response;
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String title, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Product> page = productRepository.findByTitleContaining(title,pageable);
        List<Product> productList = page.getContent();

        PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
        return response;
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        // Fetch the Category from the database:
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundExceptions("Category is not found with given Category ID"));
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        Product product = mapper.map(productDto, Product.class);
        product.setCategory(category);
        Product saveProduct = productRepository.save(product);
        return mapper.map(saveProduct, ProductDto.class);

    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundExceptions("Product is not found with given Product ID"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundExceptions("Category is not found with given Category ID"));
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        ProductDto productDto = mapper.map(savedProduct, ProductDto.class);
        return productDto;
    }

    @Override
    public PageableResponse<ProductDto> getAllOfCategories(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundExceptions("Category is not found with given Category ID"));
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Product> page = productRepository.findByCategory(category, pageable);
        List<Product> productList = page.getContent();

        PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
        return response;
    }
}