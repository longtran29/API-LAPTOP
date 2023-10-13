package com.springboot.laptop.model.dto.request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportRequestDTO {

    private List<ImportDetailDTO> importDetails;

    private String note;

}
