package com.hdfc.DTO;

import java.time.LocalDate;

/**
 * DTO for sending transaction details as response (Bank Statement).
 * Excludes reference_id as per requirement.
 */
public class TransactionResponseDTO {

    private Long transactionId;      // Unique transaction ID
    private Double amount;           // Transaction amount
    private Double availableBalance; // Balance after transaction
    private String channel;          // Transaction channel (ATM, UPI, NetBanking, etc.)
    private String fromAccount;      // Sender account number
    private String initiatedBy;      // Initiated by (customer/admin/system)
    private String remarks;          // Any remarks (e.g. Bill Payment, Transfer)
    private String status;           // Status (SUCCESS, FAILED, PENDING)
    private String toAccount;        // Receiver account number
    private LocalDate transactionTime; // When transaction happened
    private String transactionType;  // Debit / Credit
    private Long accountId;          // Account holder’s ID

    // Getters & Setters
    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public Double getAvailableBalance() { return availableBalance; }
    public void setAvailableBalance(Double availableBalance) { this.availableBalance = availableBalance; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }

    public String getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(String initiatedBy) { this.initiatedBy = initiatedBy; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }

    public LocalDate getTransactionTime() { return transactionTime; }
    public void setTransactionTime(LocalDate transactionTime) { this.transactionTime = transactionTime; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
}
