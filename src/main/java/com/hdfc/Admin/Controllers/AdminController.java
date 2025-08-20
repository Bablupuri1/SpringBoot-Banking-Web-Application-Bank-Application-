package com.hdfc.Admin.Controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hdfc.ApiResponse.ApiResponse;
import com.hdfc.DTO.CustomerAccountDTO;
import com.hdfc.DTO.CustomerResponseCredentialDTO;
import com.hdfc.DTO.DepositRequestDTO;
import com.hdfc.DTO.DepositResponseDTO;
import com.hdfc.DTO.MiniStatementDTO;
import com.hdfc.DTO.WithdrawRequestDTO;
import com.hdfc.DTO.WithdrawResponseDTO;
import com.hdfc.Services_Admin.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@PostMapping("/create-account")
	public ResponseEntity<ApiResponse<CustomerResponseCredentialDTO>> createAccount(
			@RequestBody CustomerAccountDTO requestDto) {
		return adminService.createAccount(requestDto);
	}

	@PostMapping("/deposit")
	public ResponseEntity<ApiResponse<DepositResponseDTO>> deposit(@RequestBody DepositRequestDTO request) {
		System.out.println("AdminController.deposit()");
		return adminService.depositToAccount(request);
	}

	@PostMapping("/withdraw")
	public ResponseEntity<ApiResponse<WithdrawResponseDTO>> withdrawFromAccount(WithdrawRequestDTO request) {

		ResponseEntity<ApiResponse<WithdrawResponseDTO>> withdrawFromAccount = adminService
				.withdrawFromAccount(request);

		return withdrawFromAccount;
	}
	
	
	
	
	
	
//	
//	
//	
//	
//
//	@GetMapping("/transactions/{accountId}")
//	public ResponseEntity<ApiResponse<List<TransactionResponseDTO>>> getTransactions(@PathVariable Long accountId,
//			@RequestParam String startDate, @RequestParam String endDate) {
//
//		System.out.println("AdminController.getTransactions()");
//
//		LocalDate start = LocalDate.parse(startDate); // ISO format by default
//		LocalDate end = LocalDate.parse(endDate);
//
//		
//		
//		
//		
//		List<TransactionResponseDTO> transactionsBetweenDates = adminService.getTransactionsBetweenDates(accountId,
//				start, end);
//
//		ApiResponse<List<TransactionResponseDTO>> response = new ApiResponse<>(true,
//				"Transactions fetched successfully", transactionsBetweenDates
//
//		);
//
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/gettransaction")
	public List<MiniStatementDTO> getTransactionsByAccountAndDateRange(
	        String accountNumber, String startDate, String endDate) 
	{
		
		System.out.println("AdminController.getTransactionsByAccountAndDateRange()");
		
		 // String ko LocalDateTime me convert karo
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime localDateTime1 = LocalDateTime.parse(startDate, formatter);
        System.out.println("converted date and time:"+localDateTime1);
        
        
        
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime localDateTime2= LocalDateTime.parse(endDate, formatter1);
        System.out.println("converted date and time:"+localDateTime2);
        
        
        
        List<MiniStatementDTO> transactionsByAccountAndDateRange = adminService.getTransactionsByAccountAndDateRange(accountNumber, localDateTime1, localDateTime2);

        
        System.out.println("Transaction ___________________________________");
        
        transactionsByAccountAndDateRange.forEach(System.out::println);
		
		return null;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
