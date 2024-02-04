package com.customer.shubham.dto;

import lombok.Data;

@Data
public class PageRequestDTO {
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private String state;
    private String email;
    private String phone;
    private int pageNo;
    private int pageSize;
    private String direction;
    private String property;
}
