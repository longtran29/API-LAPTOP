package com.springboot.laptop.utils;

import com.springboot.laptop.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Component;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@Component
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class FileUploadUtils {

    @Autowired private CloudinaryService cloudinaryService;


    @Operation(summary = "Tải ảnh lên",
            description = "Tải ảnh lên cloud",
            responses = {
                    @ApiResponse(description = "Tải thành công", responseCode = "200"
                   ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PostMapping("/uploadImage")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println("Da vao uploadFile");
        String url = cloudinaryService.uploadFile(file);
        return url;
    }
}
