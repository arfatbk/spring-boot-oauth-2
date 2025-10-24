package com.example.resource.mandates.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Frequency {
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    YEARLY("yearly");

    private final String frequency;

    Frequency(String frequency) {
        this.frequency = frequency;
    }

    @JsonValue
    public String getFrequency() {
        return frequency;
    }
}
