package com.springboot.laptop.handler.admin;


import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")

@CrossOrigin(origins = "http://localhost:3000")
public class BrandController {

    private final BrandService brandService;


    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ResponseEntity<?> getAllBrands() {
        System.out.println("In getAllBrand");
        List<BrandEntity> listBrands = brandService.getAll();
        System.out.println("Danh sach brand " + listBrands);
        return new ResponseEntity<List<BrandEntity>>(brandService.getAll(), HttpStatus.OK );
    }

    @PostMapping
    public ResponseEntity<?> createBrand(@RequestBody BrandEntity brand) {
        System.out.println("Brand requestbody is " + brand.getName());
        BrandEntity newBrand = brandService.createOne(brand);
        return new ResponseEntity<BrandEntity>(newBrand, HttpStatus.CREATED);
    }

    @DeleteMapping("/{brandId}")
    public ResponseEntity<String> deleteBrand(@PathVariable Long brandId) {
        brandService.deleteOne(brandId);
        return new ResponseEntity<String>("Delete successfully",HttpStatus.NO_CONTENT );
    }

    @PutMapping("/{brandId}")
    public ResponseEntity<BrandEntity> updateBrand(@PathVariable Long brandId, @RequestBody BrandEntity brand ) throws Exception {
        System.out.println("Brand requestbody is " + brand.getName());
        String name = brand.getName();
        BrandEntity brandUpdated = brandService.updateOne(brandId,name );
        return new ResponseEntity<BrandEntity>(brandUpdated, HttpStatus.CREATED);
    }

}
