package com.springboot.laptop.utils;

import org.springframework.util.StringUtils;

public class StringUtility {

    public String removeExtraSpace(String value) throws Exception {
        if(!StringUtils.hasText(value)) throw new Exception("You must provide the value");
        return value.replaceAll("\\s{2,}", " ").toLowerCase().trim();
    }
}
