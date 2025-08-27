package com.hdfc.DTO;

public class CustomerLoginRequest {
    private String customerId;
    private String password;

    // Getters & Setters
    public String getCustomerId() {
    	System.out.println("CustomerLoginRequest.getCustomerId()");
        return customerId;
    }
    public void setCustomerId(String customerId) {
    	System.out.println("CustomerLoginRequest.setCustomerId()");
        this.customerId = customerId;
    }
    public String getPassword() {
    	System.out.println("CustomerLoginRequest.getPassword()");
        return password;
    }
    public void setPassword(String password) {
    	System.out.println("CustomerLoginRequest.setPassword()");
        this.password = password;
    }
}
