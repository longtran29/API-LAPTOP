package com.springboot.laptop.model.dto.response;

import lombok.Data;

@Data
public class ReportDTO {

    private Object pending;


    private Object today;

    private Object week;

    private Object cancel;
}
