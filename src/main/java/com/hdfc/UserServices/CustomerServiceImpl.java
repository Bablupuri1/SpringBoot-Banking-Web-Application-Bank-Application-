package com.hdfc.UserServices;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hdfc.ApiResponse.ApiResponse;
import com.hdfc.DTO.BalanceResponseDTO;
import com.hdfc.DTO.TransferRequestDTO;
import com.hdfc.DTO.TransferResponseDTO;
import com.hdfc.Model.Account;
import com.hdfc.Model.Transaction;
import com.hdfc.Repositories.AccountRepository;
import com.hdfc.Repositories.TransactionRepository;
import com.hdfc.constants.MessageConstants;

/**
 * Service implementation for handling customer-related operations. This class
 * contains business logic for money transfer between accounts.
 */
@Service
public class CustomerServiceImpl implements Customer_Common_Services {

	@Autowired
	private AccountRepository accountrepo;

	@Autowired
	private TransactionRepository transactionRepo;

	/**
	 * Transfers money from one account to another.
	 *
	 * @param transferDTO Request object containing transfer details
	 * @return ResponseEntity<ApiResponse<TransferResponseDTO>>
	 */
	@Override
	public ResponseEntity<ApiResponse<TransferResponseDTO>> transferMoney(TransferRequestDTO transferDTO) {

		// STEP 1: Validate transfer amount
		double transferAmount = transferDTO.getAmount();
		if (transferAmount <= 0) {
			return new ResponseEntity<>(new ApiResponse<>(false, "Amount must be greater than 0", null),
					HttpStatus.BAD_REQUEST);
		}

		// STEP 2: Validate sender account existence
		Optional<Account> fromAccOpt = accountrepo.findByAccountNumber(transferDTO.getFromAccount());
		if (fromAccOpt.isEmpty()) {
			return new ResponseEntity<>(new ApiResponse<>(false, "Sender account does not exist", null),
					HttpStatus.NOT_FOUND);
		}

		// STEP 3: Validate receiver account existence
		Optional<Account> toAccOpt = accountrepo.findByAccountNumber(transferDTO.getToAccount());
		if (toAccOpt.isEmpty()) {
			return new ResponseEntity<>(new ApiResponse<>(false, "Receiver account does not exist", null),
					HttpStatus.NOT_FOUND);
		}

		// STEP 4: Get actual account entities
		Account fromAccount = fromAccOpt.get();
		Account toAccount = toAccOpt.get();

		// STEP 5: Check sender balance (Insufficient balance handling)
		if (fromAccount.getBalance() < transferAmount) {
			// Record failed transaction for audit/history purposes
			Transaction failedTxn = new Transaction();
			failedTxn.setAccount(fromAccount);
			failedTxn.setFromAccount(fromAccount.getAccountNumber());
			failedTxn.setToAccount(transferDTO.getToAccount());
			failedTxn.setTransactionType("DEBIT");
			failedTxn.setAmount(transferAmount);
			failedTxn.setAvailableBalance(fromAccount.getBalance());
			failedTxn.setChannel("ONLINE");
			failedTxn.setInitiatedBy(fromAccount.getCustomer().getName());
			failedTxn.setRemarks("Failed transfer to " + transferDTO.getToAccount());
			failedTxn.setStatus("FAILED");
			failedTxn.setTransactionTime(LocalDateTime.now());
			transactionRepo.save(failedTxn);

			return new ResponseEntity<>(new ApiResponse<>(false, "Sender account has insufficient balance", null),
					HttpStatus.BAD_REQUEST);
		}

		// STEP 6: Perform balance updates
		fromAccount.setBalance(fromAccount.getBalance() - transferAmount);
		toAccount.setBalance(toAccount.getBalance() + transferAmount);
		accountrepo.save(fromAccount);
		accountrepo.save(toAccount);

		// STEP 7: Record sender transaction (DEBIT)
		Transaction debitTxn = new Transaction();
		debitTxn.setAccount(fromAccount);
		debitTxn.setFromAccount(fromAccount.getAccountNumber());
		debitTxn.setToAccount(toAccount.getAccountNumber());
		debitTxn.setTransactionType("DEBIT");
		debitTxn.setAmount(transferAmount);
		debitTxn.setAvailableBalance(fromAccount.getBalance());
		debitTxn.setChannel("ONLINE");
		debitTxn.setInitiatedBy(fromAccount.getCustomer().getName());
		debitTxn.setRemarks("Transferred to " + toAccount.getAccountNumber());
		debitTxn.setStatus("SUCCESS");
		debitTxn.setTransactionTime(LocalDateTime.now());
		transactionRepo.save(debitTxn);

		// STEP 8: Record receiver transaction (CREDIT)
		Transaction creditTxn = new Transaction();
		creditTxn.setAccount(toAccount);
		creditTxn.setFromAccount(fromAccount.getAccountNumber());
		creditTxn.setToAccount(toAccount.getAccountNumber());
		creditTxn.setTransactionType("CREDIT");
		creditTxn.setAmount(transferAmount);
		creditTxn.setAvailableBalance(toAccount.getBalance());
		creditTxn.setChannel("ONLINE");
		creditTxn.setInitiatedBy(fromAccount.getCustomer().getName());
		creditTxn.setRemarks("Received from " + fromAccount.getAccountNumber());
		creditTxn.setStatus("SUCCESS");
		creditTxn.setTransactionTime(LocalDateTime.now());
		transactionRepo.save(creditTxn);

		// STEP 9: Prepare response DTO
		TransferResponseDTO responseDTO = new TransferResponseDTO();
		responseDTO.setFromAccount(fromAccount.getAccountNumber());
		responseDTO.setToAccount(toAccount.getAccountNumber());
		responseDTO.setAmount(transferAmount);
		responseDTO.setSenderBalance(fromAccount.getBalance());
		responseDTO.setReceiverBalance(toAccount.getBalance());
		responseDTO.setTransactionId(debitTxn.getReferenceId());
		responseDTO.setStatus("SUCCESS");
		responseDTO.setMessage("Transfer completed successfully");

		// STEP 10: Wrap response in ApiResponse and return
		ApiResponse<TransferResponseDTO> response = new ApiResponse<>(true, MessageConstants.MONEY_TRANSFER_SUCCESSFULL,
				responseDTO);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ApiResponse<BalanceResponseDTO>> checkBalance(String accountNumber) {
		// STEP 1: Account fetch karo

		Optional<Account> byAccountNumber = accountrepo.findByAccountNumber(accountNumber);
		Account account = byAccountNumber.get();

		// STEP 2: Agar account nahi mila
		if (account == null) {
			ApiResponse<BalanceResponseDTO> errorResponse = new ApiResponse<>(false, "Invalid account number", null);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		// public BalanceResponseDTO(Long accountNumber, String accountHolderName,
		// double availableBalance, String currency, LocalDateTime lastUpdated) {

		
		// STEP 3: Agar account mila
		BalanceResponseDTO responseDTO = new BalanceResponseDTO(account.getAccountNumber(),
				account.getCustomer().getName(), account.getBalance(), "Rupay", account.getCustomer().getDob());

		ApiResponse<BalanceResponseDTO> successResponse = new ApiResponse<>(true, "Balance fetched successfully",
				responseDTO);

		return new ResponseEntity<>(successResponse, HttpStatus.OK);
	}

}
