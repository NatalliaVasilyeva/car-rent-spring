package com.dmdev.service.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessageUtil {


    public static String getNotFoundMessage(String className, String variableName, Object variableValue) {

        return className + " with " + variableName + " " + variableValue + " does not exist.";
    }

    public static String getAlreadyExistsMessage(String className, String variableName, Object variableValue) {

        return className + " with this " + variableName + " " + variableValue + " already exist.";
    }
}