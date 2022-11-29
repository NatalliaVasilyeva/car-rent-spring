package com.dmdev.domain.dto.brand.response;

import com.dmdev.domain.dto.model.ModelResponseDto;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Value
@Builder
public class BrandResponseDto {

    @NotEmpty
    Long id;
    String name;
    Set<ModelResponseDto> models;
}