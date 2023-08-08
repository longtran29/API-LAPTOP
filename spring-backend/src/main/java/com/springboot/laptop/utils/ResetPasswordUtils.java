package com.springboot.laptop.utils;

import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.model.ResetTokenEntity;
import com.springboot.laptop.model.dto.request.ResetPasswordDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.repository.ResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class ResetPasswordUtils {

    private static ResetTokenRepository resetTokenRepository;


    // needs to defined component annotation for class
    @Autowired
    public ResetPasswordUtils(ResetTokenRepository resetTokenRepository) {
        this.resetTokenRepository = resetTokenRepository;
    }

    public static void validConstraint(ResetPasswordDTO payload) throws Exception {
        if(!StringUtils.hasText(payload.getToken()) || !StringUtils.hasText(payload.getPassword()) || !StringUtils.hasText(payload.getRetypePassword())) {
            throw new CustomResponseException((StatusResponseDTO.RESET_PASSWORD_FAILED));
        }
        ResetTokenEntity resetToken = resetTokenRepository.findByToken(payload.getToken()).orElseThrow(() -> new RuntimeException("Token is not valid"));
        // check expiration
        LocalDateTime createdDate = DateUtils.dateToLocalDateTime(resetToken.getCreatedDate());
        createdDate = createdDate.plusMinutes(10L); // the symbol "L" states for long type
        if(createdDate.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has been expired !");
        }

    }
}
