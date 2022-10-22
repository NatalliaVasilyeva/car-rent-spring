package com.dmdev;

import com.dmdev.configuration.BeansConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationRunner {
    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(BeansConfiguration.class)) {
            System.out.println(context.getBean("session"));
        }
    }
}