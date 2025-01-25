package com.example.SpringAddressAPI.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebResponse <T> {

    private String message;

    private T data;

    private Integer totalPages;
    private Long totalItems;
    private Integer currentPages;
    private Integer itemPerPage;
}
