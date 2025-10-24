package com.example.resource.upi;

import com.example.resource.mandates.dto.Bearer;
import com.example.resource.mandates.dto.CreateMandateCommand;
import com.example.resource.mandates.dto.Customer;
import com.example.resource.mandates.dto.PaymentDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreateMandateAdapterCommand {


    private BigDecimal amount;
    private String bearer;
    private Customer customer;
    private String description;
    private String frequency;
    private Integer installments;
    private Map<String, String> meta;
    @JsonProperty("original_amount")
    private BigDecimal originalAmount;
    @JsonProperty("payment_details")
    private PaymentDetails paymentDetails;
    private String product;
    @JsonProperty("start_at")
    private Long startAt;

    public static CreateMandateAdapterCommand from(CreateMandateCommand createMandateCommand) {
        LocalDateTime startDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .parse(createMandateCommand.getFirstInstallmentDate(), LocalDateTime::from)
                .toLocalDate().atTime(22,0,0);

        return CreateMandateAdapterCommand.builder()
                .amount(createMandateCommand.getAmount())
                .bearer(Bearer.MERCHANT.name())
                .customer(createMandateCommand.getCustomer().toCustomer())
                .description(createMandateCommand.getDescription())
                .frequency(createMandateCommand.getFrequency().name().toLowerCase())
                .installments(createMandateCommand.getInstallments())
                .meta(Map.of("cbAccountNumber", createMandateCommand.getMeta().getCbAccountNumber()))
                .originalAmount(createMandateCommand.getAmount())
                .paymentDetails(new PaymentDetails("upi"))
                .product("subscription")
                .startAt(startDate.toInstant(ZoneOffset.UTC).toEpochMilli())
                .build();
    }
}
