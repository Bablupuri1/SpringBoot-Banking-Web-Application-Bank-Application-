package com.hdfc.DTO;
// TransferRequestDTO.java
public class TransferRequestDTO
{
    private String fromAccount;
    private String toAccount;
    private double amounts;
    private String remarks; // optional
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
	public double getAmounts() {
		return amounts;
	}
	public void setAmounts(double amounts) {
		this.amounts = amounts;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String toString() {
		return "TransferRequestDTO [fromAccount=" + fromAccount + ", toAccount=" + toAccount + ", amounts=" + amounts
				+ ", remarks=" + remarks + "]";
	}


	



}
