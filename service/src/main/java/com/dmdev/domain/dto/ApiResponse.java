package com.dmdev.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Schema(title = "Base wrapper for api response with pagination information")
public class ApiResponse<T> {

    @Schema(description = "Actual return value of type <T>")
    private List<T> results;

    @Schema(description = "Pagination: number of items returned")
    private int size;

    @Schema(description = "Pagination: given page number")
    private int number;

    @Schema(description = "Pagination: true if this is the first page of results")
    private boolean first;

    @Schema(description = "Pagination: true if this is the last page of results")
    private boolean last;

    @Schema(description = "Pagination: number of results in given page")
    private int numberOfElements;

    @Schema(description = "Pagination: total number of elements on page")
    private long totalElements;

    @Schema(description = "Pagination: total number of pages containing results")
    private int totalPages;

    public ApiResponse(List<T> results, Page<?> page) {
        this.results = results;
        this.size = page.getSize();
        this.number = page.getNumber();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.numberOfElements = page.getNumberOfElements();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}