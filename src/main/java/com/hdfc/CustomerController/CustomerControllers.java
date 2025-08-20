package com.hdfc.CustomerController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hdfc.ApiResponse.ApiResponse;
import com.hdfc.DTO.BalanceResponseDTO;
import com.hdfc.DTO.TransferRequestDTO;
import com.hdfc.DTO.TransferResponseDTO;
import com.hdfc.UserServices.CustomerServiceImpl;

/**
 * CustomerControllers is a REST controller that handles customer-related API
 * requests. Currently, it exposes an endpoint for transferring money between
 * accounts.
 * 
 * Base URL for all APIs in this controller: /Customer_Api
 */
@RestController
@RequestMapping("/Customer_Api")
public class CustomerControllers {

	/**
	 * CustomerServiceImpl is injected here to delegate all business logic. This
	 * maintains a proper separation of concerns: - Controller = Handles HTTP
	 * requests & responses - Service = Handles business logic
	 */
	@Autowired
	private CustomerServiceImpl customerservice;

	/**
	 * API Endpoint: /Customer_Api/transfer HTTP Method: POST
	 *
	 * Purpose: To transfer money between accounts.
	 *
	 * @param transferDTO - TransferRequestDTO object containing transfer details
	 *                    (like fromAccount, toAccount, amount, etc.)
	 *
	 * @return ResponseEntity<ApiResponse<TransferResponseDTO>> - ApiResponse
	 *         wrapper contains status, message, and data - TransferResponseDTO
	 *         contains transfer result details
	 */

	@PostMapping("/transfer")
	public ResponseEntity<ApiResponse<TransferResponseDTO>> transferMoney(@RequestBody TransferRequestDTO transferDTO) {

		// Call service layer to perform transfer operation
		ResponseEntity<ApiResponse<TransferResponseDTO>> response = customerservice.transferMoney(transferDTO);

		// Return service response back to client
		return response;
	}

	@GetMapping("/checkBalance")
	public ResponseEntity<ApiResponse<BalanceResponseDTO>> checkBalance(String accountNumber) {

		ResponseEntity<ApiResponse<BalanceResponseDTO>> checkBalance = customerservice.checkBalance(accountNumber);

		return checkBalance;
	}

}
