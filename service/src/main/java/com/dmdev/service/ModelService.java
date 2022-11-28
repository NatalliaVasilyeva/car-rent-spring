package com.dmdev.service;

import com.dmdev.domain.dto.filterdto.ModelFilter;
import com.dmdev.domain.dto.model.ModelCreateRequestDto;
import com.dmdev.domain.dto.model.ModelResponseDto;
import com.dmdev.domain.dto.model.ModelUpdateRequestDto;
import com.dmdev.domain.entity.Model;
import com.dmdev.mapper.model.ModelCreateMapper;
import com.dmdev.mapper.model.ModelResponseMapper;
import com.dmdev.mapper.model.ModelUpdateMapper;
import com.dmdev.repository.ModelRepository;
import com.dmdev.service.exception.ExceptionMessageUtil;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.utils.PageableUtils;
import com.dmdev.utils.predicate.ModelPredicateBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ModelService {

    private final ModelRepository modelRepository;
    private final ModelCreateMapper modelCreateMapper;
    private final ModelUpdateMapper modelUpdateMapper;
    private final ModelResponseMapper modelResponseMapper;
    private final ModelPredicateBuilder modelPredicateBuilder;


    @Transactional
    public Optional<ModelResponseDto> create(ModelCreateRequestDto modelCreateRequestDto) {

        return Optional.of(modelCreateMapper.mapToEntity(modelCreateRequestDto))
                .map(modelRepository::save)
                .map(modelResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<ModelResponseDto> update(Long id, ModelUpdateRequestDto modelUpdateRequestDto) {
        var existingModel = getByIdOrElseThrow(id);

        return Optional.of(modelUpdateMapper.mapToEntity(modelUpdateRequestDto, existingModel))
                .map(modelRepository::save)
                .map(modelResponseMapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public Optional<ModelResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(modelResponseMapper::mapToDto);
    }


    @Transactional(readOnly = true)
    public List<ModelResponseDto> getAll() {
        return modelRepository.findAll().stream()
                .map(modelResponseMapper::mapToDto)
                .collect(toList());
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (modelRepository.existsById(id)) {
            modelRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<ModelResponseDto> getAllByBrandId(Long id) {
        return modelRepository.findModelsByBrandId(id)
                .stream()
                .map(modelResponseMapper::mapToDto)
                .collect(toList());
    }


    @Transactional(readOnly = true)
    public Page<ModelResponseDto> getAll(ModelFilter modelFilter, Integer page, Integer pageSize) {
        return modelRepository.findAll(modelPredicateBuilder.build(modelFilter), PageableUtils.getSortedPageable(page, pageSize, Sort.Direction.ASC, "brand_name")).map(modelResponseMapper::mapToDto);
    }

    private Model getByIdOrElseThrow(Long id) {
        return modelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Model", "id", id)));
    }
}