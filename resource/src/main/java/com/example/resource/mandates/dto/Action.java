package com.example.resource.mandates.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Action {

    private String id;
    private String label;
    private Boolean isEnabled;
}

