package com.springboot.laptop.model.enums;

import java.util.Arrays;

public enum UserRoleEnum {
    ROLE_ADMIN, ROLE_USER;

    public static UserRoleEnum valueOfCode(String blahCode) throws IllegalArgumentException {
        UserRoleEnum blah = Arrays.stream(UserRoleEnum.values())
                .filter(val -> val.name().equals(blahCode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unable to resolve user role: " + blahCode));

        return blah;
    }

}
