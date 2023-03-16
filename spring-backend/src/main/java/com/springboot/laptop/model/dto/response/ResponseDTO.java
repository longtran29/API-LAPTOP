package com.springboot.laptop.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {

    private ErrorCode errorCode;
    private Object data;
    private SuccessCode successCode;


    //public static khong cần initialize object
    public static ResponseDTO success(Object data){
        return new ResponseDTO(null,data,null);
    }

}
