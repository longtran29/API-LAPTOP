package com.springboot.laptop.controller;

import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.service.impl.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/upload")
public class ImageUploadController {


    private final CloudinaryService cloudinaryService;

    @Operation(summary = "Tải ảnh lên",
            description = "Tải ảnh lên cloud",
            responses = {
                    @ApiResponse(description = "Tải thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PostMapping("/image")
    public ResponseEntity<String> uploadFile(@Param("file") MultipartFile file) {
        return ResponseEntity.ok().body(cloudinaryService.uploadFile(file));
    }
}
