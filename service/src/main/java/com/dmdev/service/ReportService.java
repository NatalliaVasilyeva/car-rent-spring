package com.dmdev.service;

import com.dmdev.domain.dto.order.OrderResponseDto;
import com.dmdev.domain.dto.order.OrderUserReportDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.Writer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final OrderService orderService;

    @SneakyThrows
    public void writeUserReportsToCsv(Long userId, Writer writer) {

        List<OrderUserReportDto> orders = orderService.getAllByUserId(userId);
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

        printer.printRecord("Id", "Date", "Start rental time", "End rental time", "Brand", "Model", "Color", "Transmission",
                "Engine type", "Year", "Insurance", "Order status", "Sum");

        for (OrderUserReportDto order : orders) {
            printer.printRecord(order.getId(), order.getDate(), order.getStartRentalDate(), order.getEndRentalDate(),
                    order.getBrand(), order.getModel(), order.getColor(), order.getTransmission(), order.getEngineType(),
                    order.getYearOfProduction(), order.getInsurance(), order.getOrderStatus(), order.getSum());
        }
    }

    @SneakyThrows
    public void writeAdminOrdersReportsToCsv(Writer writer) {

        List<OrderResponseDto> orders = orderService.findAllLimitByDate();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

        printer.printRecord("Id", "Date", "Start rental time", "End rental time", "Brand", "Model", "Color", "Transmission",
                "Engine type", "Year", "Vin", "Number", "Category", "Price", "Insurance", "Order status", "Sum", "Email", "Name", "Phone");

        for (OrderResponseDto order : orders) {
            printer.printRecord(order.getId(), order.getDate(), order.getStartRentalDate(), order.getEndRentalDate(),
                    order.getCar().getBrand(), order.getCar().getModel(), order.getCar().getColor(), order.getCar().getTransmission(), order.getCar().getEngineType(),
                    order.getCar().getYearOfProduction(), order.getCar().getVin(), order.getCar().getNumber(),
                    order.getCar().getCategory(), order.getCar().getPrice(), order.getInsurance(), order.getOrderStatus(), order.getSum(),
                    order.getUser().getEmail(), order.getUser().getUserDetailsDto().getName() + " " + order.getUser().getUserDetailsDto().getSurname(), order.getUser().getUserDetailsDto().getPhone());
        }
    }
}