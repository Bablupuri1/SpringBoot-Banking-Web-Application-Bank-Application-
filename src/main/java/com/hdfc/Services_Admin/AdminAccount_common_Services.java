package com.hdfc.Services_Admin;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.hdfc.AdminDTO.PagedResponse;
import com.hdfc.AdminDTO.TransactionDto;
import com.hdfc.ApiResponse.ApiResponse;
import com.hdfc.DTO.CustomerAccountDTO;
import com.hdfc.DTO.CustomerResponseCredentialDTO;
import com.hdfc.DTO.DepositRequestDTO;
import com.hdfc.DTO.DepositResponseDTO;
import com.hdfc.DTO.MiniStatementDTO;
import com.hdfc.DTO.TransactionResponseDTO;
import com.hdfc.DTO.TransferRequestDTO;
import com.hdfc.DTO.TransferResponseDTO;
import com.hdfc.DTO.WithdrawRequestDTO;
import com.hdfc.DTO.WithdrawResponseDTO;
import com.hdfc.Model.Account;
import com.hdfc.Model.Customer;
import com.hdfc.Model.Transaction;

public interface AdminAccount_common_Services {

    //  Create Account
    public ResponseEntity<ApiResponse<CustomerResponseCredentialDTO>> createAccount(CustomerAccountDTO requestcreateaccount);

    
    //  Deposit
    public ResponseEntity<ApiResponse<DepositResponseDTO>> depositToAccount(DepositRequestDTO request);

    //  Withdraw
    public ResponseEntity<ApiResponse<WithdrawResponseDTO>> withdrawFromAccount(WithdrawRequestDTO request);

    //  Transfer Money
//    public ResponseEntity<ApiResponse<TransferResponseDTO>> transferMoney(TransferRequestDTO request);

    //  Transactions between Dates
//    public List<TransactionResponseDTO> getTransactionsBetweenDates(Long accountId, LocalDate startDate, LocalDate endDate);

    //  Mini Statement (with date range)
    public List<MiniStatementDTO> getTransactionsByAccountAndDateRange(String accountNumber, LocalDateTime startDate, LocalDateTime endDate);

    //  Transactions by Month Range
    public List<TransactionResponseDTO> getTransactionsByMonthRange(String accountNumber, String month1, String month2);

    //  Get All Accounts with Pagination
    public Page<Account> getAllAccounts(int page, int size);
    
    
    
    
    
    //this method is responsible  for transfer money between two account
    
	public ResponseEntity<ApiResponse<TransferResponseDTO>> transferMoney(TransferRequestDTO transferDTO) ;

    
    
    
    
    
    
    
    
    
    
    
    
    //get all customers with pagination
    public Page<Customer> getAllCustomers(int page, int size);

    //  Count Accounts
    public long countAccounts();

    //  Count Customers
    public long countCustomers();

    //  Count Transactions
    public long countTransactions();
    
    
    
    
    
    
    //now  get all the transaction  with pa
    
    PagedResponse<Transaction> getTransactions(
            int page, int size, String status, String type,
            String accountId, LocalDateTime fromDate, LocalDateTime toDate
        );
    
    
    
    
    //now get latest 5 recent  transaction
    
    
    
    List<TransactionDto> fetchRecentTransactions();    
    
    
    
    
    
    
}
