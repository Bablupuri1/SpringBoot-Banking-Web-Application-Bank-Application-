// Package declaration
package com.hdfc.Services_Admin;

// Import statements
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hdfc.AdminDTO.PagedResponse;
import com.hdfc.AdminDTO.TransactionDto;
import com.hdfc.Admin_Email_Services.EmailService;
import com.hdfc.ApiResponse.ApiResponse;
import com.hdfc.DTO.CustomerAccountDTO;
import com.hdfc.DTO.CustomerResponseCredentialDTO;
import com.hdfc.DTO.DepositRequestDTO;
import com.hdfc.DTO.DepositResponseDTO;
import com.hdfc.DTO.MiniStatementDTO;
import com.hdfc.DTO.TransactionResponseDTO;
import com.hdfc.DTO.WithdrawRequestDTO;
import com.hdfc.DTO.WithdrawResponseDTO;
import com.hdfc.Model.Account;
import com.hdfc.Model.Customer;
import com.hdfc.Model.Transaction;
import com.hdfc.Repositories.AccountRepository;
import com.hdfc.Repositories.CustomerRepository;
import com.hdfc.Repositories.TransactionRepository;
import com.hdfc.TransactionManagement_Helper_class.TransactionManagement;
import com.hdfc.Utils.GeneratorUtil;
import com.hdfc.constants.MessageConstants;

// Marking the class as a Spring service

@Service
public class AdminService implements AdminAccount_common_Services {

	// Injecting dependencies using Springs @Autowired annotation
	@Autowired
	private GeneratorUtil generatorUtil;

	@Autowired
	private AccountRepository accountrepo;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	TransactionRepository transactionRepo;

	@Autowired
	TransactionManagement transactionManagement;

	@Autowired
	EmailService emailservice;

	// Method to validate customer inputs

	private ResponseEntity<ApiResponse<CustomerResponseCredentialDTO>> validateCustomerInput(
			CustomerAccountDTO requestDto) {

		ApiResponse<CustomerResponseCredentialDTO> response;

		// Check if name is empty
		if (requestDto.getName() == null || requestDto.getName().trim().isEmpty()) {
			response = new ApiResponse<>(false, MessageConstants.NAME_CANNOT_BE_EMPTY, null);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}

		// Check if email is empty
		if (requestDto.getEmail() == null || requestDto.getEmail().trim().isEmpty()) {
			response = new ApiResponse<>(false, MessageConstants.EMAIL_CANNOT_BE_EMPTY, null);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		// Check if phone is empty
		if (requestDto.getPhone() == null || requestDto.getPhone().trim().isEmpty()) {
			response = new ApiResponse<>(false, MessageConstants.PHONE_CANNOT_BE_EMPTY, null);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		// Check if date of birth is null
		if (requestDto.getDob() == null) {
			response = new ApiResponse<>(false, MessageConstants.DOB_CANNOT_BE_NULL, null);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		// Check if gender is empty
		if (requestDto.getGender() == null || requestDto.getGender().trim().isEmpty()) {
			response = new ApiResponse<>(false, MessageConstants.GENDER_CANNOT_BE_EMPTY, null);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		// Check if address is empty
		if (requestDto.getAddress() == null || requestDto.getAddress().trim().isEmpty()) {
			response = new ApiResponse<>(false, MessageConstants.ADDRESS_CANNOT_BE_EMPTY, null);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		// Check if account type is empty
		if (requestDto.getAccountType() == null || requestDto.getAccountType().trim().isEmpty()) {
			response = new ApiResponse<>(false, MessageConstants.ACCOUNT_TYPE_CANNOT_BE_EMPTY, null);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		// Check if balance is negative or zero
		if (requestDto.getBalance() == 0 || requestDto.getBalance() < 0) {
			response = new ApiResponse<>(false, MessageConstants.BALANCE_MUST_BE_NON_NEGATIVE, null);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		// If all fields are valid
		return null;
	}

	@Override
	public ResponseEntity<ApiResponse<CustomerResponseCredentialDTO>> createAccount(CustomerAccountDTO requestDto) {

		// Check if email already exists
		if (customerRepository.existsByEmail(requestDto.getEmail())) {
			ApiResponse<CustomerResponseCredentialDTO> response = new ApiResponse<>(false,
					MessageConstants.EMAIL_ALREADY_EXISTS, null);
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

		// Check if phone already exists
		if (customerRepository.existsByPhone(requestDto.getPhone())) {
			ApiResponse<CustomerResponseCredentialDTO> response = new ApiResponse<>(false,
					MessageConstants.PHONE_ALREADY_EXISTS, null);
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			
			
		}

		
		
		// Validate input fields
		ResponseEntity<ApiResponse<CustomerResponseCredentialDTO>> validationResult = validateCustomerInput(requestDto);
		if (validationResult != null) {
			return validationResult;
		}

		// Create customer entity
		Customer customer = new Customer();
		customer.setName(requestDto.getName());
		customer.setEmail(requestDto.getEmail());
		customer.setPhone(requestDto.getPhone());
		customer.setGender(requestDto.getGender());
		customer.setDob(requestDto.getDob());
		customer.setAddress(requestDto.getAddress());

		// Generate customer ID and password
		String customerId = generatorUtil.generateUniqueCustomerId();
		String password = generatorUtil.generateUniquePassword();

		customer.setCustomerId(customerId);
		customer.setPassword(password); // internally stored (not returned in response)

		// Create account entity
		Account account = new Account();
		account.setAccountType(requestDto.getAccountType());
		account.setBalance(requestDto.getBalance());
		account.setAccountNumber(generatorUtil.generateAccountNumber());

		account.setCustomer(customer);
		customer.setAccount(account);

		// Save customer + account
		Customer savedCustomer = customerRepository.save(customer);

		// Record initial deposit transaction
		transactionManagement.recordTransaction(savedCustomer.getAccount(), "DEPOSIT", requestDto.getBalance(),
				"Initial account open deposit amount", "ADMIN", "ADMIN_DEPOSIT");

		// first of send email ok
		// 4. Send Email with credentials
		try {

			emailservice.sendUserCredentials(savedCustomer.getEmail(), savedCustomer.getName(),
					savedCustomer.getCustomerId(), savedCustomer.getAccount().getAccountNumber(), password,
					requestDto.getBalance(), savedCustomer.getAccount().getBalance(), false);

		} catch (Exception e) {
			e.printStackTrace(); // Email failed, but account is already created

			ApiResponse<CustomerResponseCredentialDTO> response = new ApiResponse<>(false,
					"Due To Email Server Problem Email you can send but account is opened successfully...", null);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		// Prepare response DTO
		CustomerResponseCredentialDTO responseDto = new CustomerResponseCredentialDTO(savedCustomer.getCustomerId(),
				savedCustomer.getAccount().getAccountNumber());

		// Send success response
		ApiResponse<CustomerResponseCredentialDTO> response = new ApiResponse<>(true,
				MessageConstants.ACCOUNT_CREATED_SUCCESS, responseDto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// ______________________DEPOSIT HANDLER__________________________

	@Override
	public ResponseEntity<ApiResponse<DepositResponseDTO>> depositToAccount(DepositRequestDTO request) {
		System.out.println("AdminService.depositToAccount()");

		// Find account by account number
		Optional<Account> optionalAccount = accountrepo.findByAccountNumber(request.getAccountNumber());

		if (optionalAccount.isPresent()) {
			System.out.println("Account Number Valid............");

			Account account = optionalAccount.get();

			// First of all check the given amount
			if (request.getAmount() <= 0) {
				System.out.println("Invalid amount: " + request.getAmount());
				ApiResponse<DepositResponseDTO> invalidAmountResponse = new ApiResponse<>(false,
						"Invalid amount. Please enter a positive amount.", null);
				return new ResponseEntity<>(invalidAmountResponse, HttpStatus.BAD_REQUEST);
			}

			System.out.println("Previous Balance: " + account.getBalance());

			// Update new balance
			double newBalance = account.getBalance() + request.getAmount();
			account.setBalance(newBalance);
			accountrepo.save(account); // Save updated balance
			

			// ___________________________________________________________

			// Create transaction record
			Transaction transaction = new Transaction();
			transaction.setAccount(account);
			transaction.setFromAccount("ADMIN101"); // Admin as sender
			transaction.setToAccount(account.getAccountNumber());
			transaction.setTransactionType("DEPOSIT");
			transaction.setAmount(request.getAmount());
			transaction.setAvailableBalance(newBalance);
			transaction.setChannel("BRANCH");
			transaction.setInitiatedBy("ADMIN");
			transaction.setRemarks(request.getRemarks());
			transaction.setStatus("SUCCESS");
			transaction.setTransactionTime(LocalDateTime.now());
			transaction.setDescriptioncreditanddebit("ADMIN_DEPOSIT");

			transactionRepo.save(transaction); // Save transaction

			
			
			
			
			//first of all send email to the customers for security purpose 
			//so user can easily get the iformation about bank lates bank  balance 
			// ________________________________________________________________________

			
			try {
				String name=account.getCustomer().getName();
				String email = account.getCustomer().getEmail();
				String customerId = account.getCustomer().getCustomerId();
				String accountNumber = account.getAccountNumber();
				

				emailservice.sendUserCredentials(email, name,
						customerId, accountNumber,request.getAmount(),account.getBalance());
				
			} catch (Exception e) {
				e.printStackTrace(); // Email failed, but account is already created

				ApiResponse<DepositResponseDTO> response = new ApiResponse<>(false,
						"Money Deposited successfully...", null);
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

			}
			
			
			
			
			
			
			
			// Prepare deposit response DTO
			String customerName = account.getCustomer().getName();

			DepositResponseDTO responseDTO = new DepositResponseDTO(account.getAccountNumber(), customerName,
					newBalance, MessageConstants.DEPOSITE_AMMOUNT_ADDED_IN_YOURACCOUNT);

			// Wrap in ApiResponse and return
			ApiResponse<DepositResponseDTO> response = new ApiResponse<>(true,
					MessageConstants.DEPOSITE_AMMOUNT_ADDED_IN_YOURACCOUNT, responseDTO);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} else {
			// Account not found
			ApiResponse<DepositResponseDTO> response1 = new ApiResponse<>(false,
					MessageConstants.ACCOUNT_DOES_NOT_EXISTS, null);
			return new ResponseEntity<>(response1, HttpStatus.NOT_FOUND);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	

	// _________________WITHDRAWL HANDLER______________________________

	@Override
	public ResponseEntity<ApiResponse<WithdrawResponseDTO>> withdrawFromAccount(WithdrawRequestDTO request) {

		// 1. Validate amount
		if (request.getAmount() <= 0) {
			return new ResponseEntity<>(new ApiResponse<>(false, "Amount must be greater than 0", null),
					HttpStatus.BAD_REQUEST);
		}

		// 2. Find account
		Optional<Account> accOpt = accountrepo.findByAccountNumber(request.getAccountNumber());
		if (accOpt.isEmpty()) {
			return new ResponseEntity<>(new ApiResponse<>(false, "Account not found", null), HttpStatus.NOT_FOUND);
		}

		Account account = accOpt.get();

		// 3. Check sufficient balance
		if (account.getBalance() < request.getAmount()) {
			Transaction txn = transactionManagement.recordTransaction1(account, account.getAccountNumber(),
					account.getAccountNumber(), "DEBIT", request.getAmount(), account.getBalance(), "ONLINE", "ADMIN",
					"WIDTHRAWL FAILED DUE TO INSUFFICIENT BALANCE", "FAILED", LocalDateTime.now(), "WITHDRAWAL"

			);

			// ___________WITHDRAWL FAILED THEN RETURN THE RESPONSE TO THE
			// CUSTOMER_____________________

			WithdrawResponseDTO dto = new WithdrawResponseDTO();
			dto.setAccountNumber(account.getAccountNumber());
			dto.setWithdrawnAmount(request.getAmount());
			dto.setAvailableBalance(account.getBalance());
			dto.setStatus("FAILED");
			dto.setTransactionRef(txn.getReferenceId());

			return new ResponseEntity<>(new ApiResponse<>(false, "Insufficient balance", dto), HttpStatus.BAD_REQUEST);
		}

		// 4. Deduct balance
		account.setBalance(account.getBalance() - request.getAmount());
		accountrepo.save(account);

		// __________________________This is clean code and easy to
		// maintain__________________________________________________________

		Transaction txn = transactionManagement.recordTransaction1(account, account.getAccountNumber(),
				account.getAccountNumber(), "DEBIT", request.getAmount(), account.getBalance(), "ONLINE", "ADMIN",
				"WIDTHRAWL SUCCESSFULL", "SUCCESS", LocalDateTime.now(), "WITHDRAWAL"

		);

		// _____________________________________________________________________________________

		// ____________________AFTER WITHDRAWL RETURN THE RESPONSE TO THE
		// CUSTOMER_____________________

		WithdrawResponseDTO dto = new WithdrawResponseDTO();
		dto.setAccountNumber(account.getAccountNumber());
		dto.setWithdrawnAmount(request.getAmount());
		dto.setAvailableBalance(account.getBalance());
		dto.setStatus("SUCCESS");
		dto.setTransactionRef(txn.getReferenceId());

		ApiResponse<WithdrawResponseDTO> withdrawresponse = new ApiResponse<>(true,
				MessageConstants.WITHDRWAL_TRANSFER_SUCCESSFULL, dto);

		return new ResponseEntity<>(withdrawresponse, HttpStatus.OK);

	}

	// isko dekhan h bahi pahle ka view se thik tha ye

	@Override
	public List<MiniStatementDTO> getTransactionsByAccountAndDateRange(String accountNumber, LocalDateTime startDate,
			LocalDateTime endDate) {

		System.out.println("AdminService.getTransactionsByAccountAndDateRange()");

		List<Transaction> list = transactionRepo.findTransactionsByAccountAndDateRange(accountNumber, startDate,
				endDate);

		System.out.println("Transaction details for account:" + list);

		// Step 1: Convert Transaction -> MiniStatementDTO and collect into list
		List<MiniStatementDTO> miniStatementList = list.stream().map(trans -> {
			MiniStatementDTO dto = new MiniStatementDTO();

			// Date (LocalDate)
			dto.setDate(trans.getTransactionTime().toLocalDate());

			// Description
			dto.setDescription(trans.getRemarks());

			// Debit / Credit logic
			if ("DEBIT".equalsIgnoreCase(trans.getTransactionType())) {
				dto.setDebit(trans.getAmount());
				dto.setCredit(null);
			} else {
				dto.setCredit(trans.getAmount());
				dto.setDebit(null);
			}

			// Balance
			dto.setBalance(trans.getAvailableBalance());

			return dto;
		}).collect(Collectors.toList());

		// Step 2: Debug print if needed
		System.out.println("Mini Statement DTO List: " + miniStatementList);

		// Step 3: Return final list
		return miniStatementList;

	}

	@Override
	public List<TransactionResponseDTO> getTransactionsByMonthRange(String accountNumber, String month1,
			String month2) {

		YearMonth startYm = YearMonth.parse(month1);
		YearMonth endYm = YearMonth.parse(month2);

		LocalDateTime startDate = startYm.atDay(1).atStartOfDay();
		LocalDateTime endDate = endYm.atEndOfMonth().atTime(23, 59, 59);

		// Step 1: Fetch all transactions
		List<Transaction> transactions = transactionRepo.findTransactionsByAccountAndDateRange(accountNumber, startDate,
				endDate);

		// Step 2: Convert to DTO and store in a list
		List<TransactionResponseDTO> dtoList = transactions.stream().map(tx -> {
			TransactionResponseDTO dto = new TransactionResponseDTO();
			dto.setDate(tx.getTransactionTime());
			dto.setReferenceNo(tx.getReferenceId());

			if ("FAILED".equalsIgnoreCase(tx.getStatus())) {
				dto.setDescription("FAILED - " + tx.getTransactionType());
				dto.setDebit(0.0);
				dto.setCredit(0.0);
			} else {
				dto.setDescription(tx.getTransactionType());

				if (tx.getTransactionType().equalsIgnoreCase("DEPOSIT")
						|| tx.getTransactionType().equalsIgnoreCase("CREDIT")
						|| tx.getTransactionType().equalsIgnoreCase("TRANSFER_RECEIVED")) {

					dto.setCredit(tx.getAmount());
					dto.setDebit(null);

				} else if (tx.getTransactionType().equalsIgnoreCase("DEBIT")
						|| tx.getTransactionType().equalsIgnoreCase("WITHDRAWAL")
						|| tx.getTransactionType().equalsIgnoreCase("TRANSFER_SENT")) {

					dto.setDebit(tx.getAmount());
					dto.setCredit(null);

				} else {
					dto.setDebit(null);
					dto.setCredit(null);
				}
			}

			dto.setBalance(tx.getAvailableBalance());
			return dto;
		}).toList();

		// Step 3: Return final list
		return dtoList;
	}

	// now we are going to get all account from accoun entity based on pageno and
	// page size from react

	@Override
	public Page<Account> getAllAccounts(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		return accountrepo.findAll(pageable);
	}

	// get all the customer with pagination concept

	@Override
	public Page<Customer> getAllCustomers(int page, int size) {
		// TODO Auto-generated method stub

		Pageable pageable = PageRequest.of(page, size);
		return customerRepository.findAll(pageable);
	}

	// _________get all the transaction with pagination concept_______

	@Override
	public PagedResponse<Transaction> getTransactions(int page, int size, String status, String type, String accountId,
			LocalDateTime fromDate, LocalDateTime toDate) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

		// yaha pe filtering ke liye aap `Specification` ya `custom query` use karenge
		Page<Transaction> transactionPage = transactionRepo.findAll(pageable);

		List<Transaction> transactions = transactionPage.getContent();

		return new PagedResponse<>(transactions, transactionPage.getNumber(), transactionPage.getSize(),
				transactionPage.getTotalElements(), transactionPage.getTotalPages(), transactionPage.isLast());

	}

	// now we are getting recent transaction and this method is invoked by
	// controller
	@Override
	public List<TransactionDto> fetchRecentTransactions() {
		System.out.println("AdminService.fetchRecentTransactions()");

		List<Transaction> transactions = transactionRepo.fetchRecentTransactions();

		if (transactions.isEmpty()) {
			return Collections.emptyList();
		}

		return transactions.stream().limit(10) // Optional: enforce top 10 here if not done in query
				.map(this::convertToDto).collect(Collectors.toList());
	}

	private TransactionDto convertToDto(Transaction txn) {
		TransactionDto dto = new TransactionDto();
		dto.setId(txn.getId());
		dto.setReferenceId(txn.getReferenceId());
		dto.setFromAccount(txn.getFromAccount());
		dto.setToAccount(txn.getToAccount());
		dto.setTransactionType(txn.getTransactionType());
		dto.setAmount(txn.getAmount());
		dto.setAvailableBalance(txn.getAvailableBalance());
		dto.setChannel(txn.getChannel());
		dto.setInitiatedBy(txn.getInitiatedBy());
		dto.setRemarks(txn.getRemarks());
		dto.setStatus(txn.getStatus());
		dto.setDescriptioncreditanddebit(txn.getDescriptioncreditanddebit());
		dto.setTransactionTime(txn.getTransactionTime());
		return dto;
	}

	@Override
	public long countAccounts() {
		// TODO Auto-generated method stub
		long count = accountrepo.count();
		return count;
	}

	@Override
	public long countCustomers() {
		// TODO Auto-generated method stub
		long count = customerRepository.count();

		return count;
	}

	@Override
	public long countTransactions() {
		// TODO Auto-generated method stub
		long count = transactionRepo.count();
		return count;
	}

}
