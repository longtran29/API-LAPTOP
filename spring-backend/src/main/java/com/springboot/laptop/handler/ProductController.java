package com.springboot.laptop.handler;


import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.ProductDto;
import com.springboot.laptop.model.dto.ProductResponseDto;
import com.springboot.laptop.service.CloudinaryService;
import com.springboot.laptop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public ProductController(ProductService productService, CloudinaryService cloudinaryService) {
        this.productService = productService;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping
    public ResponseEntity<ProductEntity> createProduct(@RequestBody ProductDto product) {
        System.out.println("Product brand is " + product.getBrand());
        ProductEntity createdProduct = productService.createOne(product);
        return new ResponseEntity<ProductEntity>(createdProduct, HttpStatus.CREATED);
    }

//    @PostMapping
//    public ResponseEntity<String> createProduct(@RequestBody ProductDto product) {
//        System.out.println("Product brand is " + product.getBrand());
//        return new ResponseEntity<>("Add sucesss", HttpStatus.CREATED);
//    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        String url = cloudinaryService.uploadFile(file);
        return url;
    }


    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return new ResponseEntity<List<ProductResponseDto>>(productService.getAll(), HttpStatus.OK);
    }


}
