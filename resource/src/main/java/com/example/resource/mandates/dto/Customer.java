package com.example.resource.mandates.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("mobile_number")
    private String mobileNumber;

    private String name;
}

