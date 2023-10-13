package com.springboot.laptop.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.springboot.laptop.service.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AmazonS3ServiceImpl implements AmazonS3Service {

    private final AmazonS3 amazonS3;

    @Value("${userBucket.path}")
    private String userBucketPath;

    @Value("${aws.image.folder}")
    private String imageFolderName;

    @Override
    public String uploadImage(MultipartFile multipartFile) {

        String path = String.format("%s/%s", userBucketPath, imageFolderName);
        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.addUserMetadata("Content-Type", multipartFile.getContentType());
//        objectMetadata.addUserMetadata("Content-Length", String.valueOf(multipartFile.getSize()));
        try {
            amazonS3.putObject(path, multipartFile.getOriginalFilename(),multipartFile.getInputStream(), objectMetadata);
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to upload the file", e);
        }
        return getObjectUrl(path, multipartFile.getOriginalFilename());
    }

    private String getObjectUrl(String bucketName,String fileName) {
        String url =amazonS3.getUrl(bucketName, fileName).toExternalForm();;
        return url;
    }
}
