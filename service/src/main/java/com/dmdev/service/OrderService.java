package com.dmdev.service;

import com.dmdev.domain.dto.filterdto.OrderFilter;
import com.dmdev.domain.dto.order.OrderCreateRequestDto;
import com.dmdev.domain.dto.order.OrderResponseDto;
import com.dmdev.domain.dto.order.OrderUpdateRequestDto;
import com.dmdev.domain.dto.order.OrderUserReportDto;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.domain.entity.Order;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.model.OrderStatus;
import com.dmdev.mapper.order.OrderCreateMapper;
import com.dmdev.mapper.order.OrderReportMapper;
import com.dmdev.mapper.order.OrderResponseMapper;
import com.dmdev.mapper.order.OrderUpdateMapper;
import com.dmdev.repository.CarRentalTimeRepository;
import com.dmdev.repository.CarRepository;
import com.dmdev.repository.OrderRepository;
import com.dmdev.repository.UserRepository;
import com.dmdev.service.exception.ExceptionMessageUtil;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.service.exception.OrderBadRequestException;
import com.dmdev.utils.PageableUtils;
import com.dmdev.utils.predicate.OrderPredicateBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    @Value("${app.insurance.percent}")
    private final int insurance_percent;
    private final OrderRepository orderRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final CarRentalTimeRepository carRentalTimeRepository;
    private final OrderCreateMapper orderCreateMapper;
    private final OrderUpdateMapper orderUpdateMapper;
    private final OrderResponseMapper orderResponseMapper;
    private final OrderReportMapper orderReportMapper;
    private final OrderPredicateBuilder orderPredicateBuilder;


    @Transactional
    public Optional<OrderResponseDto> create(OrderCreateRequestDto orderCreateRequestDto) {
        checkIsCarExists(orderCreateRequestDto.getCarId());
        checkIsUserExists(orderCreateRequestDto.getUserId());

        getCarByIdWithLockOrElseThrow(orderCreateRequestDto.getCarId());

        if (!ensureCarAvailable(orderCreateRequestDto.getCarId(), orderCreateRequestDto.getStartRentalDate(), orderCreateRequestDto.getEndRentalDate())) {
            return Optional.empty();
        }

        var user = getUserById(orderCreateRequestDto.getUserId());
        var driverLicenseOptional = getDriverLicenseByUser(user);
        if (driverLicenseOptional.isEmpty() || driverLicenseOptional.get().getExpiredDate().isBefore(orderCreateRequestDto.getStartRentalDate().toLocalDate())) {
            return Optional.empty();
        }

        BigDecimal price = getPriceByCarId(orderCreateRequestDto.getCarId()).get();
        long numberOfDays = calculateNumberOfDays(orderCreateRequestDto.getStartRentalDate(), orderCreateRequestDto.getEndRentalDate());

        BigDecimal orderSum = orderCreateRequestDto.getInsurance() ? calculateOrderSumWithInsurance(numberOfDays, price, calculateInsuranceSum(numberOfDays, price)) :
                calculateOrderSumWithoutInsurance(numberOfDays, price);

        var order = orderCreateMapper.mapToEntity(orderCreateRequestDto);
        order.setSum(orderSum);

        return Optional.of(order)
                .map(orderRepository::save)
                .map(orderResponseMapper::mapToDto);
    }


    @Transactional
    public Optional<OrderResponseDto> update(Long id, OrderUpdateRequestDto dto) {
        getCarByIdOrElseThrow(dto.getCarId());

        var existingOrder = getByIdOrElseThrow(id);
        var existingCarRentalTime = getCarRentalTimeByOrderIdOrElseThrow(id);

        if (!ensureCarAvailableByOrderId(id, dto.getCarId(), dto.getStartRentalDate(), dto.getEndRentalDate())) {
            return Optional.empty();
        }

        boolean isInsuranceNeededExisting = existingOrder.isInsurance();
        boolean isInsuranceNeededToUpdate = dto.getInsurance();

        if (isRentalDateChanged(dto, existingCarRentalTime) ||
                isInsuranceNeededExisting != isInsuranceNeededToUpdate) {
            BigDecimal price = findPriceByOrder(existingOrder).get();
            long numberOfDays = calculateNumberOfDays(dto.getStartRentalDate(), dto.getEndRentalDate());
            if (isInsuranceNeededToUpdate) {
                BigDecimal insuranceSum = calculateInsuranceSum(numberOfDays, price);
                existingOrder.setSum(calculateOrderSumWithInsurance(numberOfDays, price, insuranceSum));
            } else {
                existingOrder.setSum(calculateOrderSumWithoutInsurance(numberOfDays, price));
            }
        }

        return Optional.of(orderUpdateMapper.mapToEntity(dto, existingOrder))
                .map(orderRepository::save)
                .map(orderResponseMapper::mapToDto);
    }


    public Optional<OrderResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(orderResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<OrderResponseDto> changeOrderStatus(Long id, OrderStatus status) {
        var existingOrder = getByIdOrElseThrow(id);

        existingOrder.setOrderStatus(status);

        return Optional.of(existingOrder)
                .map(orderRepository::save)
                .map(orderResponseMapper::mapToDto);
    }

    public Page<OrderResponseDto> getAll(OrderFilter orderFilter, Integer page, Integer pageSize) {
        return orderRepository.findAll(
                        orderPredicateBuilder.build(orderFilter), PageableUtils.getSortedPageable(page, pageSize, Sort.Direction.ASC, "date"))
                .map(orderResponseMapper::mapToDto);
    }

    public List<OrderResponseDto> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderResponseMapper::mapToDto)
                .collect(toList());
    }

    public List<OrderResponseDto> findAllLimitByDate() {
        return orderRepository.findAllLimitByDate(LocalDate.now().minusDays(30))
                .stream()
                .map(orderResponseMapper::mapToDto)
                .collect(toList());
    }

    public List<OrderResponseDto> getAllByStatus(OrderStatus status) {
        return orderRepository.findAllByOrderStatus(status)
                .stream()
                .map(orderResponseMapper::mapToDto)
                .collect(toList());
    }

    public List<OrderResponseDto> getAllByUserId(Long userId, OrderStatus status, BigDecimal sum) {
        return StreamSupport.stream(orderRepository.findAll(
                        orderPredicateBuilder.usersBuild(userId, status, sum)).spliterator(), false)
                .map(orderResponseMapper::mapToDto)
                .collect(toList());
    }

    public List<OrderUserReportDto> getAllByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(orderReportMapper::mapToDto)
                .collect(toList());
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void checkIsCarExists(Long carId) {
        if (!carRepository.existsById(carId)) {
            throw new OrderBadRequestException(String.format(ExceptionMessageUtil.getNotFoundMessage("Car", "id", carId)));
        }
    }

    private Car getCarByIdOrElseThrow(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Car", "id", carId)));
    }

    private Car getCarByIdWithLockOrElseThrow(Long carId) {
        return carRepository.findByIdWithLock(carId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Car", "id", carId)));
    }

    private void checkIsUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new OrderBadRequestException(String.format(ExceptionMessageUtil.getNotFoundMessage("User", "id", userId)));
        }
    }

    private Order getByIdOrElseThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Order", "id", id)));
    }

    private CarRentalTime getCarRentalTimeByOrderIdOrElseThrow(Long orderId) {
        return carRentalTimeRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Order", "id", orderId)));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new OrderBadRequestException(String.format(ExceptionMessageUtil.getNotFoundMessage("User", "id", userId))));
    }

    private boolean ensureCarAvailable(Long carId, LocalDateTime startDate, LocalDateTime endDate) {
        return carRepository.isCarAvailable(carId, startDate, endDate);
    }

    private boolean ensureCarAvailableByOrderId(Long orderId, Long carId, LocalDateTime start, LocalDateTime end) {
        return carRepository.isCarAvailableByOrderId(orderId, carId, start, end);
    }

    private Optional<DriverLicense> getDriverLicenseByUser(User user) {
        return user.getUserDetails()
                .getDriverLicenses()
                .stream().findFirst();
    }

    private Optional<BigDecimal> getPriceByCarId(Long carId) {
        return carRepository.findById(carId)
                .map(Car::getCategory)
                .map(Category::getPrice);
    }

    private Optional<BigDecimal> findPriceByOrder(Order order) {
        return carRepository.findById(order.getCar().getId())
                .map(Car::getCategory)
                .map(Category::getPrice);
    }

    private long calculateNumberOfDays(LocalDateTime start, LocalDateTime end) {
        Duration period = Duration.between(end, start);
        return Math.abs(period.toDays());
    }

    private BigDecimal calculateOrderSumWithInsurance(long days, BigDecimal rentCarPricePerDay, BigDecimal insuranceSum) {
        return new BigDecimal(rentCarPricePerDay.intValue() * days + insuranceSum.intValue());
    }

    private BigDecimal calculateOrderSumWithoutInsurance(long days, BigDecimal rentCarPricePerDay) {
        return new BigDecimal(rentCarPricePerDay.intValue() * days);
    }

    private boolean isRentalDateChanged(OrderUpdateRequestDto dto, CarRentalTime existing) {
        return dto.getStartRentalDate() != existing.getStartRentalDate() ||
                dto.getEndRentalDate() != existing.getEndRentalDate();
    }

    private BigDecimal calculateInsuranceSum(long days, BigDecimal rentCarPricePerDay) {
        long insurance = rentCarPricePerDay.intValue() * days * insurance_percent / 100;
        return new BigDecimal(insurance);
    }
}