package com.hdfc.AdminDTO;

import java.util.List;

public class AccountPageResponse {
	
	private List<AccountDTO> accounts;
	private int totalPages;
	private int currentPage;
	private int pageSize;
	private long totalElements;
	private boolean isFirst;
	private boolean isLast;

	
	public List<AccountDTO> getAccounts() {
		return accounts;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public void setAccounts(List<AccountDTO> accountDTOs) {
		this.accounts = accountDTOs;


	}

}
