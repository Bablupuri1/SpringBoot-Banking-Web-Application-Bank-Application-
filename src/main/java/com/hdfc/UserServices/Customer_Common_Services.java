package com.hdfc.UserServices;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.hdfc.ApiResponse.ApiResponse;
import com.hdfc.DTO.BalanceResponseDTO;
import com.hdfc.DTO.TransactionResponseDTO;
import com.hdfc.DTO.TransactionResponseHistroyDTO;
import com.hdfc.DTO.TransferRequestDTO;
import com.hdfc.DTO.TransferResponseDTO;

public interface Customer_Common_Services {

	
//	_______________________________________________________________________________________________________
	
	
	public ResponseEntity<ApiResponse<TransferResponseDTO>> transferMoney(TransferRequestDTO transferDTO);

    public ResponseEntity<ApiResponse<BalanceResponseDTO>> checkBalance(String accountNumber);

    
//    _______________________________________________________________________________________________
    
    
    //this is working and final bahut achha kam kar rha h kuki bahut achha response a rha h
    
	public List<TransactionResponseDTO> getTransactionsByMonthRange(String accountNumber, String month1, String month2);

//________________________________________________________________________________________________________


	//This method is also working
//	  public List<TransactionResponseHistroyDTO> getTransactionsByAccountAndDateRange(
//	            String accountNumber,
//	            LocalDateTime startDate,
//	            LocalDateTime endDate
//	    );
//

	
//	___________________get the start date and end date from html use local-date input tag_________________________________________________________________

	public List<TransactionResponseHistroyDTO> getTransactionsByAccountAndDateRange(
            String accountNumber,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

//	______________THIS IS USEFUL WHEN CUSTOMER ALL HISTORY SPECIFIC TO ACCOUNT_______________________________________________________________________________________


	

	public List<TransactionResponseHistroyDTO> findTransactionsByAccount(String accountNumber) ;



//	___________________________________________________________________


	



}
