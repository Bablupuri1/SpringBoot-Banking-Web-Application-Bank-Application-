package com.hdfc.CustomerController;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hdfc.ApiResponse.ApiResponse;
import com.hdfc.DTO.BalanceResponseDTO;
import com.hdfc.DTO.TransactionResponseDTO;
import com.hdfc.DTO.TransactionResponseHistroyDTO;
import com.hdfc.DTO.TransferRequestDTO;
import com.hdfc.DTO.TransferResponseDTO;
import com.hdfc.UserServices.CustomerServiceImpl;

/**
 * CustomerControllers is a REST controller that handles all customer-related
 * API requests. Base URL: /Customer_Api
 */

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/Customer_Api")
public class CustomerControllers {

	@Autowired
	private CustomerServiceImpl customerservice;

	
	// ------------------------------------------------------------------------------------------------------------
	// POST: Transfer money between accounts
	// ------------------------------------------------------------------------------------------------------------
	@PostMapping("/transfer")
	public ResponseEntity<ApiResponse<TransferResponseDTO>> transferMoney(@RequestBody TransferRequestDTO transferDTO) {
		System.out.println("CustomerControllers.transferMoney()");
		System.out.println("CustomerControllers.transferMoney()" + transferDTO.toString());
		return customerservice.transferMoney(transferDTO);
	}

	
	
	
	
	// ------------------------------------------------------------------------------------------------------------
	// GET: Check balance of an account
	// ------------------------------------------------------------------------------------------------------------
	@GetMapping("/checkBalance")
	public ResponseEntity<ApiResponse<BalanceResponseDTO>> checkBalance(String accountNumber) {
		return customerservice.checkBalance(accountNumber);
	}

	
	
	
	// ------------------------------------------------------------------------------------------------------------
	// GET: Transactions by month range (HTML input type="month")
	// ------------------------------------------------------------------------------------------------------------
	@GetMapping("/getTransactionMonth")
	@ResponseBody
	public List<TransactionResponseDTO> getTransactionsByMonthRange(@RequestParam String accountNumber,
			@RequestParam String month1, @RequestParam String month2) {

		System.out.println("CustomerControllers.getTransactionsByMonthRange()");

		return customerservice.getTransactionsByMonthRange(accountNumber, month1, month2);
	}
	
	
	

	// ------------------------------------------------------------------------------------------------------------
	// GET: Transaction history by date-time range (HTML input
	// type="datetime-local")
	// Example format: 2025-08-24T00:00
	// this is used in admin controller which is used to show the account specific
	// transaction ok
	// ------------------------------------------------------------------------------------------------------------
	@GetMapping("/getTransactionBetweenRange")
	public List<TransactionResponseHistroyDTO> getTransactionHistory(@RequestParam String accountNumber,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

		// Extend endDate to include full day
		endDate = endDate.withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);

		return customerservice.getTransactionsByAccountAndDateRange(accountNumber.trim(), startDate, endDate);
	}

	// ------------------------------------------------------------------------------------------------------------
	// GET: All transactions by account number prefect working response************
	// use showing for netbanikng page when use login
	// ------------------------------------------------------------------------------------------------------------
	@GetMapping("/getTransactionHistory/{accountNumber}")
	public ResponseEntity<ApiResponse<List<TransactionResponseHistroyDTO>>> getTransactionsByAccountNumber(
			@PathVariable String accountNumber) {

		System.out.println("CustomerControllers.getTransactionsByAccountNumber()");

		List<TransactionResponseHistroyDTO> transactions = customerservice.findTransactionsByAccount(accountNumber);

		if (transactions == null || transactions.isEmpty()) {
			ApiResponse<List<TransactionResponseHistroyDTO>> response = new ApiResponse<>(false,
					"No transactions found for account " + accountNumber, transactions);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		ApiResponse<List<TransactionResponseHistroyDTO>> response = new ApiResponse<>(true,
				"Transactions fetched successfully", transactions);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
