package com.hdfc.AdminDTO;

import java.time.LocalDate;

import com.hdfc.Model.Account;

public class AccountDTO {
    private Long id;
    private String accountNumber;
    private String accountType;
    private double balance;
    private boolean active;
    private LocalDate createdAt;
    private String customerName;

    // Constructor to map from Account entity
    public AccountDTO(Account account) {
        this.id = account.getId();
        this.accountNumber = account.getAccountNumber();
        this.accountType = account.getAccountType();
        this.balance = account.getBalance();
        this.active = account.isActive();
        this.createdAt = account.getCreatedAt();
        this.customerName = account.getCustomer() != null ? account.getCustomer().getName() : null;
    }
    
    
    public AccountDTO() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

    // Getters and Setters
    
    
}
