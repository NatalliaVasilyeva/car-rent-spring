package com.dmdev.mapper.model;

import com.dmdev.domain.dto.model.ModelUpdateRequestDto;
import com.dmdev.domain.entity.Model;
import com.dmdev.mapper.UpdateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModelUpdateMapper implements UpdateMapper<ModelUpdateRequestDto, Model> {

    @Override
    public Model mapToEntity(ModelUpdateRequestDto requestDto, Model existing) {
        merge(requestDto, existing);
        return existing;
    }

    @Override
    public void merge(ModelUpdateRequestDto requestDto, Model existing) {
        existing.setName(requestDto.getName());
        existing.setTransmission(requestDto.getTransmission());
        existing.setEngineType(requestDto.getEngineType());
    }
}