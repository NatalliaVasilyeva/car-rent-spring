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
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandCreateEditMapper brandCreateEditMapper;
    private final BrandUpdateMapper brandUpdateMapper;
    private final BrandResponserMapper brandResponserMapper;

    @Transactional
    public Optional<BrandResponseDto> create(BrandCreateEditRequestDto brandCreateEditRequestDto) {
        checkBrandNameIsUnique(brandCreateEditRequestDto.getName());

        var brand = brandCreateEditMapper.map(brandCreateEditRequestDto);
        return Optional.of(brandResponserMapper
                .map(brandRepository
                        .save(brand)));
    }

    @Transactional
    public Optional<BrandResponseDto> update(Long id, BrandCreateEditRequestDto brandCreateEditRequestDto) {
        var existingBrand = getByIdOrElseThrow(id);

        if (!existingBrand.getName().equals(brandCreateEditRequestDto.getName())) {
            checkBrandNameIsUnique(brandCreateEditRequestDto.getName());
        }

        return Optional.of(
                        brandRepository.save(
                                brandUpdateMapper.map(brandCreateEditRequestDto, existingBrand)))
                .map(brandResponserMapper::map);
    }

    @Transactional(readOnly = true)
    public Optional<BrandResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(brandResponserMapper::map);
    }

    @Transactional(readOnly = true)
    public Page<BrandResponseDto> getAll(Integer page, Integer pageSize) {
        var pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "name");
        return brandRepository.findAll(pageRequest)
                .map(brandResponserMapper::map);
    }

    @Transactional(readOnly = true)
    public List<BrandResponseDto> getAll() {
        return brandRepository.findAll()
                .stream()
                .map(brandResponserMapper::mapNames)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<BrandFullView> getAllFullView() {
        return brandRepository.findAllFull();
    }

    @Transactional(readOnly = true)
    public Optional<BrandFullView> getByIdFullView(Long brandId) {
        return brandRepository.findByIdAllFull(brandId);
    }

    @Transactional(readOnly = true)
    public List<BrandFullView> getByNameFullView(String name) {
        return brandRepository.findByNameAllFull(name);
    }

    @Transactional(readOnly = true)
    public Optional<BrandResponseDto> getByName(String name) {
        return brandRepository.findByName(name)
                .map(brandResponserMapper::map);
    }

    @Transactional(readOnly = true)
    public List<BrandResponseDto> getByNames(List<String> names) {
        return brandRepository.findByNameInIgnoringCase(names)
                .stream()
                .map(brandResponserMapper::map)
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
                .orElseThrow(() -> new NotFoundException(String.format("Brand with id %s does not exist.", id)));
    }

    private void checkBrandNameIsUnique(String brandName) {
        if (brandRepository.existsByNameIgnoringCase(brandName)) {
            throw new BrandBadRequestException(String.format("Brand with this name '%s' already exists", brandName));
        }
    }
}