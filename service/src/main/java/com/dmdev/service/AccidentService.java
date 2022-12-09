package com.dmdev.service;

import com.dmdev.domain.dto.accident.AccidentCreateRequestDto;
import com.dmdev.domain.dto.accident.AccidentResponseDto;
import com.dmdev.domain.dto.accident.AccidentUpdateRequestDto;
import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.Order;
import com.dmdev.mapper.accident.AccidentCreateMapper;
import com.dmdev.mapper.accident.AccidentResponseMapper;
import com.dmdev.mapper.accident.AccidentUpdateMapper;
import com.dmdev.repository.AccidentRepository;
import com.dmdev.repository.OrderRepository;
import com.dmdev.service.exception.ExceptionMessageUtil;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.utils.PageableUtils;
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
@Transactional(readOnly = true)
public class AccidentService {

    private final OrderRepository orderRepository;
    private final AccidentRepository accidentRepository;
    private final AccidentCreateMapper accidentCreateMapper;
    private final AccidentUpdateMapper accidentUpdateMapper;
    private final AccidentResponseMapper accidentResponseMapper;


    @Transactional
    public Optional<AccidentResponseDto> create(AccidentCreateRequestDto accidentCreateRequestDto) {
        ensureOrderExistsById(accidentCreateRequestDto.getOrderId());

        return Optional.of(accidentCreateMapper.mapToEntity(accidentCreateRequestDto))
                .map(accidentRepository::save)
                .map(accidentResponseMapper::mapToDto);
    }


    @Transactional
    public Optional<AccidentResponseDto> update(Long id, AccidentUpdateRequestDto dto) {
        var existingAccident = findByIdOrThrow(id);

        return Optional.of(accidentUpdateMapper.mapToEntity(dto, existingAccident))
                .map(accidentRepository::save)
                .map(accidentResponseMapper::mapToDto);
    }


    public Optional<AccidentResponseDto> getById(Long id) {
        return Optional.of(findByIdOrThrow(id))
                .map(accidentResponseMapper::mapToDto);
    }


    public Page<AccidentResponseDto> getAll(Integer page, Integer pageSize) {
        return accidentRepository.findAll(PageableUtils.getSortedPageable(page, pageSize, Sort.Direction.ASC, "accidentDate"))
                .map(accidentResponseMapper::mapToDto);
    }

    public List<AccidentResponseDto> findAllByOrderId(Long orderId) {
        return accidentRepository.findAllByOrderId(orderId)
                .stream()
                .map(accidentResponseMapper::mapToDto)
                .collect(toList());
    }

    public List<AccidentResponseDto> findAllByCarNumber(String carNumber) {
        return accidentRepository.findAllByCarNumber(carNumber)
                .stream()
                .map(accidentResponseMapper::mapToDto)
                .collect(toList());
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (accidentRepository.existsById(id)) {
            accidentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Order ensureOrderExistsById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Order", "id", id)));
    }

    private Accident findByIdOrThrow(Long id) {
        return accidentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Accident", "id", id)));
    }
}