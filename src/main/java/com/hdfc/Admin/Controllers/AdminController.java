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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hdfc.ApiResponse.ApiResponse;
import com.hdfc.DTO.CustomerAccountDTO;
import com.hdfc.DTO.CustomerResponseCredentialDTO;
import com.hdfc.DTO.DepositRequestDTO;
import com.hdfc.DTO.DepositResponseDTO;
import com.hdfc.DTO.MiniStatementDTO;
import com.hdfc.DTO.TransactionResponseDTO;
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





	@GetMapping("/gettransaction")
	public List<MiniStatementDTO> getTransactionsByAccountAndDateRange(String accountNumber, String startDate,
			String endDate) {

		System.out.println("AdminController.getTransactionsByAccountAndDateRange()");

		// String ko LocalDateTime me convert karo
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime localDateTime1 = LocalDateTime.parse(startDate, formatter);
		System.out.println("converted date and time:" + localDateTime1);

		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime localDateTime2 = LocalDateTime.parse(endDate, formatter1);
		System.out.println("converted date and time:" + localDateTime2);

		List<MiniStatementDTO> transactionsByAccountAndDateRange = adminService
				.getTransactionsByAccountAndDateRange(accountNumber, localDateTime1, localDateTime2);

		System.out.println("Transaction ___________________________________");

		transactionsByAccountAndDateRange.forEach(System.out::println);

		return null;
	}

	// this is usefule for get the transaction between two date and moth year not
	// time

	// here no need to specify the name of @RequestParam("accno") becouse incomming
	// url varibale name and method varibale name same h yha pe



	@GetMapping("/getTransactionMonth")
	@ResponseBody
	public List<TransactionResponseDTO> getTransactionsByMonthRange(@RequestParam String accountNumber,
			@RequestParam String month1, @RequestParam String month2) {

		List<TransactionResponseDTO> transactionsByMonthRange = adminService.getTransactionsByMonthRange(accountNumber,
				month1, month2);


		return transactionsByMonthRange;

	}

}
