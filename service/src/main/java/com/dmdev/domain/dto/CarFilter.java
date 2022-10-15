package com.dmdev.domain.dto;

import com.dmdev.domain.model.Color;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CarFilter {
    Color color;
    Integer year;
    String modelName;
    String brandName;
    String carNumber;
    String category;
    String price;
    boolean repaired;

}