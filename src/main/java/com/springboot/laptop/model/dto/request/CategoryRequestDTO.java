package com.springboot.laptop.model.dto.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CategoryRequestDTO {

    private String name;

    private Boolean enabled = true;

}
