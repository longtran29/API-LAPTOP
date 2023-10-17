package com.springboot.laptop.service;


import com.amazonaws.services.s3.S3ClientOptions;
import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3Service {

    public String uploadImage(MultipartFile multipartFile);

//    public String getObjectUrl();
}
