package com.hdfc.DTO;

import java.time.LocalDateTime;

public class TransactionResponseDTO {

    private LocalDateTime date;       // Transaction date & time
    private String description;       // e.g. "Salary Credit", "ATM Withdrawal"
    private String referenceNo;       // Unique Ref/Txn/Cheque No.
    private Double debit;             // Debit Amount (if money goes out)
    private Double credit;            // Credit Amount (if money comes in)
    private Double balance;           // Available balance after txn

    // --- Getters and Setters ---
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getReferenceNo() {
        return referenceNo;
    }
    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }
    public Double getDebit() {
        return debit;
    }
    public void setDebit(Double debit) {
        this.debit = debit;
    }
    public Double getCredit() {
        return credit;
    }
    public void setCredit(Double credit) {
        this.credit = credit;
    }
    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
