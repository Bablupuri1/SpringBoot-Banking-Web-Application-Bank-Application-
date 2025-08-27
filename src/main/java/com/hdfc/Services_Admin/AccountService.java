package com.hdfc.Services_Admin;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.hdfc.ApiResponse.ApiResponse;
import com.hdfc.DTO.CustomerAccountDTO;
import com.hdfc.DTO.CustomerResponseCredentialDTO;
import com.hdfc.DTO.DepositRequestDTO;
import com.hdfc.DTO.DepositResponseDTO;
import com.hdfc.DTO.MiniStatementDTO;
import com.hdfc.DTO.TransactionResponseDTO;
import com.hdfc.DTO.WithdrawRequestDTO;
import com.hdfc.DTO.WithdrawResponseDTO;

public interface AccountService {

    /**
     * Creates a new account for the customer.
     *
     * @param requestDto the DTO containing customer and account creation data
     * @return ApiResponse containing status and created account/customer data
     */
	public ResponseEntity<ApiResponse<CustomerResponseCredentialDTO>> createAccount(CustomerAccountDTO requestcreateaccount);
    public ResponseEntity<ApiResponse<DepositResponseDTO>> depositToAccount(DepositRequestDTO request);


    //implement feature measn after some days
	//public ResponseEntity<ApiResponse<TransferResponseDTO>> transferMoney(TransferRequestDTO request) ;


	public ResponseEntity<ApiResponse<WithdrawResponseDTO>> withdrawFromAccount(WithdrawRequestDTO request) ;


//    public List<TransactionResponseDTO > getTransactionsBetweenDates(Long accountId, LocalDate startDate, LocalDate endDate);





    List<MiniStatementDTO> getTransactionsByAccountAndDateRange(
            String accountNumber, LocalDateTime startDate, LocalDateTime endDate);









    //this is useful when account holder want to know the transaction between
    //such as 2024-01-10 to 2025-02-21
    public List<TransactionResponseDTO> getTransactionsByMonthRange(String accountNumber, String month1, String month2);
















}
