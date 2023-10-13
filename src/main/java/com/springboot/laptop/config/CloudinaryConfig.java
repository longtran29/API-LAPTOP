package com.springboot.laptop.config;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CloudinaryConfig {
    private static final String API_SECRET = "TxTcg6DontHBgGQ8CW0z6iVu10Q";
    private static final String API_KEY = "716182341599247";
    private static final String CLOUD_NAME = "dhkf8una1";

    public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }


}
