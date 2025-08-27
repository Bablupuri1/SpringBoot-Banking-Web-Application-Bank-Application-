
package com.hdfc.DTO;
public class TransactionResponseHistroyDTO {

    private Long txnId;
    private String fromAccount;
    private String toAccount;
    private String type;
    private String amount; // Optional: can keep for generic use
    private String debitAmount;
    private String creditAmount;
    private String transactionDate;
    private String transactionTime;
    private String status;
    private String remarks;
    private double balance;
    private String referenceid;
    private String description;
	public Long getTxnId() {
		return txnId;
	}
	public void setTxnId(Long txnId) {
		this.txnId = txnId;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(String debitAmount) {
		this.debitAmount = debitAmount;
	}
	public String getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(String creditAmount) {
		this.creditAmount = creditAmount;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getReferenceid() {
		return referenceid;
	}
	public void setReferenceid(String referenceid) {
		this.referenceid = referenceid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "TransactionResponseHistroyDTO [txnId=" + txnId + ", fromAccount=" + fromAccount + ", toAccount="
				+ toAccount + ", type=" + type + ", amount=" + amount + ", debitAmount=" + debitAmount
				+ ", creditAmount=" + creditAmount + ", transactionDate=" + transactionDate + ", transactionTime="
				+ transactionTime + ", status=" + status + ", remarks=" + remarks + ", balance=" + balance
				+ ", referenceid=" + referenceid + ", description=" + description + "]";
	}

    // Getters and Setters
    
    
    
}
