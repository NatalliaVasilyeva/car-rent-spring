package com.dmdev.service;

import com.dmdev.domain.dto.brand.request.BrandCreateEditRequestDto;
import com.dmdev.domain.dto.brand.response.BrandResponseDto;
import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.projection.BrandFullView;
import com.dmdev.mapper.brand.BrandCreateEditMapper;
import com.dmdev.mapper.brand.BrandResponserMapper;
import com.dmdev.mapper.brand.BrandUpdateMapper;
import com.dmdev.repository.BrandRepository;
import com.dmdev.service.exception.BrandBadRequestException;
import com.dmdev.service.exception.ExceptionMessageUtil;
import com.dmdev.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandCreateEditMapper brandCreateEditMapper;
    private final BrandUpdateMapper brandUpdateMapper;
    private final BrandResponserMapper brandResponserMapper;

    @Transactional
    public Optional<BrandResponseDto> create(BrandCreateEditRequestDto brandCreateEditRequestDto) {
        checkBrandNameIsUnique(brandCreateEditRequestDto.getName());

        return Optional.of(brandCreateEditMapper.mapToEntity(brandCreateEditRequestDto))
                .map(brandRepository::save)
                .map(brandResponserMapper::mapToDto);
    }

    @Transactional
    public Optional<BrandResponseDto> update(Long id, BrandCreateEditRequestDto brandCreateEditRequestDto) {
        var existingBrand = getByIdOrElseThrow(id);

        if (!existingBrand.getName().equals(brandCreateEditRequestDto.getName())) {
            checkBrandNameIsUnique(brandCreateEditRequestDto.getName());
        }

        return Optional.of(brandUpdateMapper.mapToEntity(brandCreateEditRequestDto, existingBrand))
                .map(brandRepository::save)
                .map(brandResponserMapper::mapToDto);
    }


    public Optional<BrandResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(brandResponserMapper::mapToDto);
    }


    public Page<BrandResponseDto> getAll(Integer page, Integer pageSize) {
        var pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "name");
        return brandRepository.findAll(pageRequest)
                .map(brandResponserMapper::mapToDto);
    }


    public List<BrandResponseDto> getAll() {
        return brandRepository.findAll().stream()
                .map(brandResponserMapper::mapToDto)
                .collect(toList());
    }


    public List<BrandFullView> getAllFullView() {
        return brandRepository.findAllFullView();
    }


    public Optional<BrandFullView> getByIdFullView(Long brandId) {
        return brandRepository.findAllByIdFullView(brandId);
    }


    public List<BrandFullView> getByNameFullView(String name) {
        return brandRepository.findAllByNameFullView(name);
    }


    public Optional<BrandResponseDto> getByName(String name) {
        return brandRepository.findByName(name)
                .map(brandResponserMapper::mapToDto);
    }


    public List<BrandResponseDto> getByNames(List<String> names) {
        return brandRepository.findByNameInIgnoringCase(names).stream()
                .map(brandResponserMapper::mapToDto)
                .collect(toList());
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (brandRepository.existsById(id)) {
            brandRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Brand getByIdOrElseThrow(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Brand", "id", id)));
    }

    private void checkBrandNameIsUnique(String brandName) {
        if (brandRepository.existsByNameIgnoringCase(brandName)) {
            throw new BrandBadRequestException(ExceptionMessageUtil.getAlreadyExistsMessage("Brand", "name", brandName));
        }
    }
}