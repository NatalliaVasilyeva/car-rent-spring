package com.dmdev.api.controller;

import com.dmdev.domain.dto.filterdto.OrderFilter;
import com.dmdev.domain.dto.order.OrderCreateRequestDto;
import com.dmdev.domain.dto.order.OrderResponseDto;
import com.dmdev.domain.dto.order.OrderUpdateRequestDto;
import com.dmdev.domain.model.OrderStatus;
import com.dmdev.service.CarService;
import com.dmdev.service.OrderService;
import com.dmdev.service.UserService;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.service.exception.UserBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping(path = "/orders")
@RequiredArgsConstructor
public class OrderApi {

    private static final String SUCCESS_ATTRIBUTE = "success_message";
    private static final String ERROR_ATTRIBUTE = "error_message";
    private final OrderService orderService;
    private final CarService carService;

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    public String createOrder(@RequestParam(value = "carId") Long carId, Model model, @ModelAttribute OrderCreateRequestDto order) {
        model.addAttribute("order", order);
        model.addAttribute("car", carService.getById(carId).get());
        return "layout/order/create-order";
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    public String create(@ModelAttribute OrderCreateRequestDto requestDto,
                         RedirectAttributes redirectedAttributes) {
        Optional<OrderResponseDto> order = orderService.create(requestDto);
        if (order.isEmpty()) {
            redirectedAttributes.addFlashAttribute(ERROR_ATTRIBUTE, "Car is unavailable for these dates. Please choose other dates or car");
            return "redirect:/cars";
        }

        redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "You create order successfully.");
        return "redirect:/orders/" + order.get().getId();
    }


    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute OrderUpdateRequestDto orderUpdateRequestDto,
                         Model model,
                         RedirectAttributes redirectedAttributes) {
        Optional<OrderResponseDto> order = orderService.update(id, orderUpdateRequestDto);

        if (order.isEmpty()) {
            redirectedAttributes.addFlashAttribute(ERROR_ATTRIBUTE, "Car is unavailable for these dates. Please choose other dates or car");
        }
        model.addAttribute("cars", carService.getAll());
        return "redirect:/orders/{id}";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findById(@PathVariable("id") Long id, Model model) {
        return orderService.getById(id)
                .map(order -> {
                    model.addAttribute("order", order);
                    model.addAttribute("cars", carService.getAll());
                    return "layout/order/order";
                })
                .orElseThrow(() -> new NotFoundException(String.format("Order with id %s does not exist.", id)));
    }

    @PostMapping("/{id}/change-status")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String changeStatus(@PathVariable("id") Long id,
                               @RequestParam OrderStatus status) {
        return orderService.changeOrderStatus(id, status)
                .map(result -> "redirect:/orders/{id}")
                .orElseThrow(() -> new UserBadRequestException("Order status has not been changed"));
    }


    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAll(Model model,
                          @ModelAttribute OrderFilter orderFilter,
                          @RequestParam(required = false, defaultValue = "0") Integer page,
                          @RequestParam(required = false, defaultValue = "20") Integer size) {

        var ordersPage = orderService.getAll(orderFilter, page, size);
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("filter", orderFilter);
        model.addAttribute("statuses", OrderStatus.values());

        return "layout/order/orders";
    }

    @GetMapping("/by-status")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllByStatus(Model model,
                                  @ModelAttribute OrderFilter orderFilter,
                                  @RequestParam OrderStatus status) {

        var orders = orderService.getAllByStatus(status);
        var ordersPage = new PageImpl<>(orders);
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("filter", orderFilter);
        model.addAttribute("statuses", OrderStatus.values());

        return "layout/order/orders";
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    public String findAllByUserId(Model model,
                                  @PathVariable("id") Long id,
                                  @RequestParam @Nullable OrderStatus status,
                                  @RequestParam @Nullable BigDecimal sum) {

        var userOrders = orderService.getAllByUserId(id, status, sum);
        var ordersPage = new PageImpl<>(userOrders);
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("statuses", OrderStatus.values());

        return "layout/order/user-orders";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (!orderService.deleteById(id)) {
            throw new NotFoundException(String.format("Order with id %s does not exist.", id));
        }
        return "redirect:/orders";
    }
}