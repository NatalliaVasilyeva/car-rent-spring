package com.dmdev.mapper.model;

import com.dmdev.domain.dto.model.ModelResponseDto;
import com.dmdev.domain.entity.Model;
import com.dmdev.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ModelResponseMapper implements ResponseMapper<Model, ModelResponseDto> {

    @Override
    public ModelResponseDto mapToDto(Model model) {
        return ModelResponseDto.builder()
                .id(model.getId())
                .brand(model.getBrand().getName())
                .name(model.getName())
                .transmission(model.getTransmission())
                .engineType(model.getEngineType())
                .build();
    }

    public Set<ModelResponseDto> mapToDtos(Set<Model> models) {
        return models.stream()
                .map(this::mapToDto)
                .collect(Collectors.toSet());
    }
}