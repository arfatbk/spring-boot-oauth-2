package com.example.resource.mandates.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Preferences {

    @JsonProperty("tnc_url")
    private String tncUrl;

    @JsonProperty("support_number")
    private String supportNumber;

    @JsonProperty("skip_checkout_consent")
    private Boolean skipCheckoutConsent;

    @JsonProperty("enterprise_handler_entity")
    private String enterpriseHandlerEntity;
}

