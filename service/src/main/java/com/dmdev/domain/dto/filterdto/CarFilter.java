package com.dmdev.domain.dto.filterdto;

import com.dmdev.domain.model.Color;
import com.dmdev.domain.model.EngineType;
import com.dmdev.domain.model.Transmission;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CarFilter {

    Color color;
    Integer year;
    List<String> modelNames;
    List<String> brandNames;
    List<String> categoryNames;
    Transmission transmission;
    EngineType engineType;
}