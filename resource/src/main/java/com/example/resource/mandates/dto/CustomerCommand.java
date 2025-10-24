package com.example.resource.mandates.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCommand {
    private String mobileNumber;
    private String name;

    public Customer toCustomer() {
        Customer customer = new Customer();
        customer.setName(this.getName());
        customer.setMobileNumber(this.getMobileNumber());
        return customer;
    }
}