package com.application.foggy.api.v1.customers;

import lombok.Builder;
import lombok.Data;

@Data
public class Address {
    private String address;
    private String city;
    private String state;
    private String pinCode;
}
