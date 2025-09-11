package com.hdfc.Admin.Controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hdfc.AdminDTO.AccountDTO;
import com.hdfc.AdminDTO.AccountPageResponse;
import com.hdfc.AdminDTO.CustomerDTO;
import com.hdfc.AdminDTO.CustomersPageResponse;
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
import com.hdfc.Services_Admin.AdminService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173") // सिर्फ React के लिए allow
public class AdminController {

	// private AdminService1 adminService;

	// this is testing jab maine count kiya tha account,etc

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
	public ResponseEntity<ApiResponse<WithdrawResponseDTO>> withdrawFromAccount(@RequestBody WithdrawRequestDTO request) {

		System.out.println("AdminController.withdrawFromAccount()");
		ResponseEntity<ApiResponse<WithdrawResponseDTO>> withdrawFromAccount = adminService
				.withdrawFromAccount(request);

		return withdrawFromAccount;
	}

	
	//now we need to create api for Transfer Money Between Two account
	
	
	
	
	@PostMapping("/transferBetweenAccount")
	public ResponseEntity<ApiResponse<TransferResponseDTO>> transferMoney(@RequestBody TransferRequestDTO transferDTO) {
		System.out.println("CustomerControllers.transferMoney()");
		System.out.println("CustomerControllers.transferMoney()" + transferDTO.toString());
		return adminService.transferMoney(transferDTO);
	}
	
	
	
	
	
	
	@GetMapping("/getTransactionAccountByRange")
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

	// ____________________________________________________________

	@GetMapping("/getAllAccount")
	public AccountPageResponse getPaginatedAccounts(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		// Step 1: Fetch paginated Account entities
		Pageable pageable = PageRequest.of(page, size);
		Page<Account> allAccounts = adminService.getAllAccounts(page, size);

		// Step 2: Extract content into a separate variable
		List<Account> accountEntities = allAccounts.getContent();

		// Step 3: Convert each Account to AccountDTO using setter methods
		List<AccountDTO> accountDTOs = new ArrayList<>();
		for (Account account : accountEntities) {
			AccountDTO dto = new AccountDTO();
			dto.setId(account.getId());
			dto.setAccountNumber(account.getAccountNumber());
			dto.setAccountType(account.getAccountType());
			dto.setBalance(account.getBalance());
			dto.setActive(account.isActive());
			dto.setCreatedAt(account.getCreatedAt());
			dto.setCustomerName(account.getCustomer() != null ? account.getCustomer().getName() : null);

			accountDTOs.add(dto);
		}

		// Step 4: Populate response DTO
		AccountPageResponse response = new AccountPageResponse();

		response.setAccounts(accountDTOs);

		response.setTotalPages(allAccounts.getTotalPages());
		response.setCurrentPage(allAccounts.getNumber());
		response.setPageSize(allAccounts.getSize());
		response.setTotalElements(allAccounts.getTotalElements());
		response.setFirst(allAccounts.isFirst());
		response.setLast(allAccounts.isLast());

		return response;
	}

	// create api for get all the customers with pagination concept

	@GetMapping("/getAllCustomers")
	public CustomersPageResponse getAllCustomersWithPagination(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		// Step 1: Create pageable and fetch paginated data
		Pageable pageable = PageRequest.of(page, size);
		Page<Customer> customerPage = adminService.getAllCustomers(page, size);

		// Step 2: Extract content
		List<Customer> customerEntities = customerPage.getContent();

		// Step 3: Convert each Customer to CustomerDTO
		List<CustomerDTO> customerDTOs = new ArrayList<>();
		for (Customer customer : customerEntities) {
			CustomerDTO dto = new CustomerDTO();
			dto.setCustomerId(customer.getCustomerId());
			dto.setName(customer.getName());
			dto.setEmail(customer.getEmail());
			dto.setAddress(customer.getAddress());
			dto.setDob(customer.getDob());
			dto.setGender(customer.getGender());
			dto.setPhone(customer.getPhone());
			dto.setRole(customer.getRole());
			customerDTOs.add(dto);
		}

		// Step 4: Prepare response DTO
		CustomersPageResponse response = new CustomersPageResponse();
		response.setCustomers(customerDTOs);
		response.setTotalPages(customerPage.getTotalPages());
		response.setCurrentPage(customerPage.getNumber());
		response.setPageSize(customerPage.getSize());
		response.setTotalElements(customerPage.getTotalElements());
		response.setFirst(customerPage.isFirst());
		response.setLast(customerPage.isLast());

		return response;
	}

	
	
	
	
	
	
	
	
	// get all the transaction
	@GetMapping("/getAllTransactions")
	public ResponseEntity<PagedResponse<Transaction>> getTransactions(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String status,
			@RequestParam(required = false) String type, @RequestParam(required = false) String accountId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
		return ResponseEntity.ok(adminService.getTransactions(page, size, status, type, accountId, fromDate, toDate));
	}

	
	
	
	
	
	
	
	
	
	
	// we are going to developed recent transaction
	@GetMapping("/RecentTransaction")
	public ResponseEntity<List<TransactionDto>> getRecentTransactions() {
		System.out.println("AdminController.getRecentTransactions()");
		List<TransactionDto> transactions = adminService.fetchRecentTransactions();
		return ResponseEntity.ok(transactions);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// create api for get the ano of account

	@GetMapping("/getnoofAccounts")
	public ResponseEntity<?> getnoofAccounts() {
		long countAccounts = adminService.countAccounts();

		return new ResponseEntity<Long>(countAccounts, HttpStatus.OK);

	}

	// api for get no of customers

	@GetMapping("/getnoofCustomers")
	public ResponseEntity<?> getnoofCustomers() {
		long countCustomers = adminService.countCustomers();

		return new ResponseEntity<Long>(countCustomers, HttpStatus.OK);

	}

	@GetMapping("/getnoofTransactions")
	public ResponseEntity<?> getnoofTransactions() {
		long countTransactions = adminService.countTransactions();

		return new ResponseEntity<Long>(countTransactions, HttpStatus.OK);

	}

}
