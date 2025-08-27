package com.hdfc.DTO;

import java.time.LocalDate;

/**
 * DTO for Mini Statement Response (like SBI ATM).
 */
public class MiniStatementDTO {

    private LocalDate date;         // Transaction date
    private String description;     // Narration / Remarks (UPI/ATM/NEFT etc.)
    private Double debit;           // Amount debited (if any)
    private Double credit;          // Amount credited (if any)
    private Double balance;         // Available balance after transaction

    // Getters & Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getDebit() { return debit; }
    public void setDebit(Double debit) { this.debit = debit; }

    public Double getCredit() { return credit; }
    public void setCredit(Double credit) { this.credit = credit; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
	@Override
	public String toString() {
		return "MiniStatementDTO [date=" + date + ", description=" + description + ", debit=" + debit + ", credit="
				+ credit + ", balance=" + balance + "]";
	}





}
