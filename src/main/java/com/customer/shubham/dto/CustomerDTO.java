package com.customer.shubham.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private int customerId;
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private String state;
    private String email;
    private String phone;
}
