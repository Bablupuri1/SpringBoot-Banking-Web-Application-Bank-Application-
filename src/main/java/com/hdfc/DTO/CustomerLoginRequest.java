package com.hdfc.DTO;

import lombok.Data;

@Data
public class CustomerLoginRequest {
    private String customerId;
    private String password;
}