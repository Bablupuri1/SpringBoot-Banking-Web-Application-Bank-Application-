package com.hdfc.AdminDTO;

import java.time.LocalDateTime;

public class TransactionDto {
    private Long id;
    private String referenceId;
    private String fromAccount;
    private String toAccount;
    private String transactionType;
    private double amount;
    private double availableBalance;
    private String channel;
    private String initiatedBy;
    private String remarks;
    private String status;
    private String descriptioncreditanddebit;
    private LocalDateTime transactionTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}
	public String getToAccount() {
		return toAccount;
	}
	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getInitiatedBy() {
		return initiatedBy;
	}
	public void setInitiatedBy(String initiatedBy) {
		this.initiatedBy = initiatedBy;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescriptioncreditanddebit() {
		return descriptioncreditanddebit;
	}
	public void setDescriptioncreditanddebit(String descriptioncreditanddebit) {
		this.descriptioncreditanddebit = descriptioncreditanddebit;
	}
	public LocalDateTime getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(LocalDateTime transactionTime) {
		this.transactionTime = transactionTime;
	}
    
    
    
    
    
}
