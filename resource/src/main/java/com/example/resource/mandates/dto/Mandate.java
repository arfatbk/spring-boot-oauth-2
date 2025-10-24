package com.example.resource.mandates.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mandate {

    private String id;
    private BigDecimal amount;

    @JsonProperty("amount_without_charges")
    private BigDecimal amountWithoutCharges;

    private String bearer;

    @JsonProperty("discount_id")
    private String discountId;

    @JsonProperty("charge_id")
    private String chargeId;

    @JsonProperty("installment_amount")
    private Integer installmentAmount;

    private Frequency frequency;
    private Integer installments;
    private String product;

    @JsonProperty("mandate_url")
    private String mandateUrl;

    @JsonProperty("payment_details")
    private PaymentDetails paymentDetails;

    private Customer customer;
    private String merchant;

    @JsonProperty("merchant_info")
    private MerchantInfo merchantInfo;

    private String description;

    @JsonProperty("amount_remaining")
    private Integer amountRemaining;

    @JsonProperty("installments_paid")
    private Integer installmentsPaid;

    @JsonProperty("next_charge_at")
    private Long nextChargeAt;

    @JsonProperty("gateway_mandate_id")
    private String gatewayMandateId;

    @JsonProperty("start_at")
    private Long startAt;

    @JsonProperty("end_at")
    private Long endAt;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("updated_at")
    private Long updatedAt;

    @JsonProperty("is_deleted")
    private Boolean isDeleted;

    private String state;
    private String status;

    @JsonProperty("original_amount")
    private BigDecimal originalAmount;

    private Map<String, Object> meta;
    private List<Action> actions;

    @JsonProperty("reference_id")
    private String referenceId;

    @JsonProperty("reference_type")
    private String referenceType;
}
