package com.dmdev.domain.dto.filterdto;

import com.dmdev.domain.model.EngineType;
import com.dmdev.domain.model.Transmission;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ModelFilter {

    List<String> brands;
    List<String> models;
    List<String> categories;
    Transmission transmission;
    EngineType engineType;
}