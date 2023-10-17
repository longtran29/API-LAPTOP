package com.springboot.laptop.model.dto.response;

import com.springboot.laptop.model.dto.LinkDTO;
import com.springboot.laptop.model.enums.OrderPaypalStatus;
import lombok.Data;

import java.util.List;
@Data
public class OrderedResponseDTO {

    private String id;
    private OrderPaypalStatus status;
    private List<LinkDTO> links;
}
