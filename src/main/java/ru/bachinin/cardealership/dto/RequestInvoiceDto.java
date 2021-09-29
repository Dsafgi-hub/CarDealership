package ru.bachinin.cardealership.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class RequestInvoiceDto {
    @NotNull
    private Long id_user;

    @NotEmpty
    private List<LinkedHashMap<String, String>> vehicles;
}
