package com.springboot.laptop.model.dto.response;


import com.springboot.laptop.model.Account;
import com.springboot.laptop.model.ImportDetail;
import com.springboot.laptop.model.Invoice;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter

public class ImportResponseDTO {

    private Long id;

    private Date import_Date;

//    private AccountResponseDTO account;

    private Account account;

    private List<ImportDetail> importDetails;

    private Invoice invoice;

    private String note;


}
