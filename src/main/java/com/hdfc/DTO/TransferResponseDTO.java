package com.hdfc.DTO;

public class TransferResponseDTO {
	private String fromAccount;
	private String toAccount;
	private double TransferAmount;
	private double senderAvailableBalance; // renamed
	private String message;
	private String referenceId;
	private String status;
	private String InitiatedBy;
	
	
	
	
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
	public double getTransferamount() {
		return TransferAmount;
	}
	public void setTransferamount(double transferamount) {
		TransferAmount = transferamount;
	}
	public double getSenderAvailableBalance() {
		return senderAvailableBalance;
	}
	public void setSenderAvailableBalance(double senderAvailableBalance) {
		this.senderAvailableBalance = senderAvailableBalance;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInitiatedBy() {
		return InitiatedBy;
	}
	public void setInitiatedBy(String initiatedBy) {
		InitiatedBy = initiatedBy;
	}

}
