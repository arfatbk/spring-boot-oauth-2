package com.example.resource.mandates.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantInfo {

    private String id;
    private String name;

    @JsonProperty("mobile_number")
    private String mobileNumber;

    @JsonProperty("created_at")
    private Long createdAt;
}

