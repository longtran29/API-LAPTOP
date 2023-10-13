package com.springboot.laptop.controller;


import com.springboot.laptop.service.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/image-upload")
@RequiredArgsConstructor
public class AmazonUploadController {

    private final AmazonS3Service amazonS3Service;

    @PostMapping(
//            path = "",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Object saveTodo(@RequestParam("file") MultipartFile file) {
        return  ResponseEntity.ok(amazonS3Service.uploadImage(file));
    }



}
