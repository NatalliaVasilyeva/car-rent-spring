package com.dmdev.utils;

import lombok.experimental.UtilityClass;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.stream.Stream;

@UtilityClass
public final class AppUtils {

    public static String generateHash(String login, String password) {
        byte[] salt = login.getBytes();
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] encoded = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(encoded);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException();
        }
    }

    public static <T> boolean checkNull(T object) {
        return Stream.of(object.getClass().getDeclaredFields()).allMatch(f -> {
            try {
                f.setAccessible(true);
                return f.get(object) == null || f.get(object) == "";
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}