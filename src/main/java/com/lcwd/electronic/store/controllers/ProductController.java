package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/products")
@Api(value = "ProductController", description = "REST APIs related to perform product operations !!")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imagePath;


    Logger logger = LoggerFactory.getLogger(ProductController.class);
    // Create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ApiOperation(value = "Add a new new Product !!")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 401, message = "not authorized !!"),
            @ApiResponse(code = 201, message = "new user created !!")
    })
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto) {
        ProductDto productDto1 = productService.create(productDto);
        return new ResponseEntity<>(productDto1, HttpStatus.CREATED);
    }

    //Update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    @ApiOperation(value = "Update single product details by productId!! || Only Admin can update product !!")
    public ResponseEntity<ProductDto> update(@PathVariable String productId, @RequestBody ProductDto productDto) {
        ProductDto updateProductDto = productService.update(productDto, productId);
        return new ResponseEntity<>(updateProductDto, HttpStatus.OK);
    }

    //Get All
    @GetMapping
    @ApiOperation(value = "Get all Products !!")
    public ResponseEntity<PageableResponse<ProductDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> productLists = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productLists, HttpStatus.OK);
    }

    // Get single Product
    @GetMapping("/{productId}")
    @ApiOperation(value = "Get single Product by productId !!")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String productId) {
        ProductDto productDto = productService.get(productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    // Delete Product
    @DeleteMapping("/{productId}")
    @ApiOperation(value = "Delete single Product by productId !!")
    public ResponseEntity<ApiResponseMessage> deleteProductById(@PathVariable String productId) {
        productService.delete(productId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Product delete successfully").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/live")
    @ApiOperation(value = "Get all live products !!")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> productLists = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productLists, HttpStatus.OK);
    }

    @GetMapping("/search/{query}")
    @ApiOperation(value = "Search products by keywords!!")
    public ResponseEntity<PageableResponse<ProductDto>> search(
            @PathVariable String query,
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> productLists = productService.searchByTitle(query, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productLists, HttpStatus.OK);
    }

    // Upload product image
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{productId}")
    @ApiOperation(value = "Upload products image  by productId || Only Admin can upload !!")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @PathVariable String productId,
            @RequestParam("productImage") MultipartFile image
    ) throws IOException {
        String fileName = fileService.uploadFile(image, imagePath);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ "+fileName);
        logger.info("File name : {}", fileName);
        ProductDto productDto = productService.get(productId);
        productDto.setProductImageName(fileName);
        logger.info("ProductImage : {}", productDto.getProductImageName());
        ProductDto updatedProduct = productService.update(productDto, productId);
        ImageResponse response = ImageResponse.builder().imageName(updatedProduct.getProductImageName()).message("Product image is successfully uploaded !!").status(HttpStatus.CREATED).success(true).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


    //serve Product image
    @GetMapping(value = "/image/{productId}")
    @ApiOperation(value = "Get Product image by productId !!")
    public void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto productDto = productService.get(productId);
        InputStream resource = fileService.getResource(imagePath, productDto.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
