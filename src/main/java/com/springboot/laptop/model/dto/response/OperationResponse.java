package com.springboot.laptop.model.dto.response;

import lombok.Data;

@Data
public class OperationResponse {
    private StatusResponseDTO  operationStatus;

    private String  operationMessage;
}
