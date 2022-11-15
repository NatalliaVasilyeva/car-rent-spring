package com.dmdev.utils;

import com.dmdev.service.exception.RentCarException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

@UtilityClass
public final class ValidationUtils {

    public static <T> boolean checkNull(T object) {
        return Stream.of(object.getClass().getDeclaredFields()).allMatch(f -> {
            try {
                f.setAccessible(true);
                return f.get(object) == null || f.get(object) == "";
            } catch (IllegalAccessException e) {
                throw new RentCarException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while checking object for null.");
            }
        });
    }
}