package com.example.resource.mandates.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class CreateMandateCommand {

   private final BigDecimal amount;
    private final CustomerCommand customer;
    private final String description;
    private final Frequency frequency;
    private final Integer installments;
    private final Meta meta;
    private final String firstInstallmentDate;

}
