package com.hdfc.DTO;

public class CustomerResponseCredentialDTO {

    private String customerId;
    private String accountNumber;

    public CustomerResponseCredentialDTO() {}

    public CustomerResponseCredentialDTO(String customerId, String accountNumber) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
    }

    // Getters and setters
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
