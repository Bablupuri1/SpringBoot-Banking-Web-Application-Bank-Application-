package com.hdfc.DTO;

public class WithdrawRequestDTO {
    private String accountNumber;
    private double amount;

    // Getters and Setters
    public String getAccountNumber() {
    	System.out.println("WithdrawRequestDTO.getAccountNumber()");
    	
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
    	System.out.println("WithdrawRequestDTO.setAccountNumber()");
        this.accountNumber = accountNumber;
    }
    public double getAmount() {
    	System.out.println("WithdrawRequestDTO.getAmount()");
        return amount;
    }
    public void setAmount(double amount) {
    	System.out.println("WithdrawRequestDTO.setAmount()");
        this.amount = amount;
    }
}
