package com.dmdev.api.controller;

import com.dmdev.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping(path = "/reports")
@RequiredArgsConstructor
public class ReportApi {

    private final ReportService reportService;


    @GetMapping("/{id}/create")
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    public void createUserReport(@PathVariable("id") Long id,
                                 HttpServletResponse response) throws IOException {

        String reportName = "id_" + id + "_date_" + LocalDate.now() +".csv";
        response.setContentType("text/csv");

        response.addHeader("Content-Disposition", "attachment; filename=" + reportName);

        reportService.writeUserReportsToCsv(id, response.getWriter());
    }
}