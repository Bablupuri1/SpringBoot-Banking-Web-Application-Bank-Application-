package com.hdfc.DTO;

import java.time.LocalDate;

/**
 * DTO class to carry balance response details
 */
public class BalanceResponseDTO {

	private String accountNumber; // Account number of customer
	private String accountHolderName; // Name of account holder
	private double availableBalance; // Available balance in account
	private String currency; // Currency type (e.g., INR, USD)
	private LocalDate lastUpdated; // Last balance updated timestamp

	// Constructor
	public BalanceResponseDTO(String accnumber, String accountHolderName, double availableBalance, String currency,
			LocalDate lastUpdated) {
		this.accountNumber = accnumber;
		this.accountHolderName = accountHolderName;
		this.availableBalance = availableBalance;
		this.currency = currency;
		this.lastUpdated = lastUpdated;
	}

	// Getters and Setters
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getCurrency() {
		return currency;
	}
	

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public LocalDate getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDate lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
