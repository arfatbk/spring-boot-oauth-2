package com.example.resource.mandates.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreateMandateResponse {

    private final String mandateId;
    private final String mandateDeepLink;
    private final String qrCode;

}
