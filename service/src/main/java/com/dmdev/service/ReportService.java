package com.dmdev.service;

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

        List<OrderUserReportDto> orders = orderService.findAllByUserId(userId);
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

        printer.printRecord("Id", "Date", "Start rental time", "End rental time", "Brand", "Model", "Color", "Transmission",
                "Engine type", "Year", "Insurance", "Order status", "Sum");

        for (OrderUserReportDto order : orders) {
            printer.printRecord(order.getId(), order.getDate(), order.getStartRentalDate(), order.getEndRentalDate(),
                    order.getBrand(), order.getModel(), order.getColor(), order.getTransmission(), order.getEngineType(),
                    order.getYearOfProduction(), order.getInsurance(), order.getOrderStatus(), order.getOrderStatus());
        }
    }
}