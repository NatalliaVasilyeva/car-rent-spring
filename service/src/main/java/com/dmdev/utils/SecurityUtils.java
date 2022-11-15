package com.dmdev.utils;

import com.dmdev.service.exception.RentCarException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@UtilityClass
public final class SecurityUtils {

    public static String securePassword(String login, String password) {
        byte[] salt = login.getBytes();
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] encoded = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(encoded);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RentCarException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while securing password.");
        }
    }
}