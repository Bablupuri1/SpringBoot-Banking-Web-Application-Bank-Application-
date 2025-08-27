
package com.hdfc.Model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Column(name = "reference_id", nullable = false)
    private String referenceId = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private String fromAccount;
    private String toAccount;

    @Column(nullable = false)
    private String transactionType; // DEPOSIT, WITHDRAWAL, TRANSFER_SENT, TRANSFER_RECEIVED

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private double availableBalance;

    private String channel;
    private String initiatedBy;
    private String remarks;

    @Column(nullable = false)
    private String status; // SUCCESS, FAILED, PENDING

    private String descriptioncreditanddebit;
 

    private LocalDateTime transactionTime = LocalDateTime.now();

    // ----------------- Getters and Setters -----------------


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

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
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

	public LocalDateTime getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(LocalDateTime transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getDescriptioncreditanddebit() {
		return descriptioncreditanddebit;
	}

	public void setDescriptioncreditanddebit(String descriptioncreditanddebit) {
		this.descriptioncreditanddebit = descriptioncreditanddebit;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", referenceId=" + referenceId + ", account=" + account + ", fromAccount="
				+ fromAccount + ", toAccount=" + toAccount + ", transactionType=" + transactionType + ", amount="
				+ amount + ", availableBalance=" + availableBalance + ", channel=" + channel + ", initiatedBy="
				+ initiatedBy + ", remarks=" + remarks + ", status=" + status + ", descriptioncreditanddebit="
				+ descriptioncreditanddebit + ", transactionTime=" + transactionTime + "]";
	}

    // (rest of your existing getters and setters remain unchanged)
    
    
}
