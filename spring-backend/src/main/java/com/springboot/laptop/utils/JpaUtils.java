package com.springboot.laptop.utils;

import org.springframework.lang.Nullable;

import java.util.Optional;

public class JpaUtils {

    private JpaUtils() {}

    @Nullable
    public static String getString(@Nullable Object object) {
        return Optional.ofNullable(object)
                .map(String::valueOf)
                .orElse(null);
    }

    @Nullable
    public static Long getLong(@Nullable Object object) {
        return Optional.ofNullable(getString(object))
                .map(Long::valueOf)
                .orElse(null);
    }
}
