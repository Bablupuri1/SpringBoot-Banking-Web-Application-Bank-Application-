package com.hdfc.UserServices;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hdfc.ApiResponse.ApiResponse;
import com.hdfc.DTO.BalanceResponseDTO;
import com.hdfc.DTO.TransactionResponseDTO;
import com.hdfc.DTO.TransactionResponseHistroyDTO;
import com.hdfc.DTO.TransferRequestDTO;
import com.hdfc.DTO.TransferResponseDTO;
import com.hdfc.Model.Account;
import com.hdfc.Model.Transaction;
import com.hdfc.Repositories.AccountRepository;
import com.hdfc.Repositories.TransactionRepository;
import com.hdfc.Utils.MaskAccount;
import com.hdfc.constants.MessageConstants;

/**
 * Service implementation for handling customer-related operations. Contains
 * business logic for money transfer, balance check, and transaction history.
 */
@Service
public class CustomerServiceImpl implements Customer_Common_Services {

	@Autowired
	private AccountRepository accountrepo;

	@Autowired
	private TransactionRepository transactionRepo;
	

	
	// ------------------------------------------------------------------------------------------------------------
	// TRANSFER MONEY
	// ------------------------------------------------------------------------------------------------------------
	@Override
	public ResponseEntity<ApiResponse<TransferResponseDTO>> transferMoney(TransferRequestDTO transferDTO) {

		System.out.println("CustomerServiceImpl.transferMoney()");
		double transferAmount = transferDTO.getAmounts();

		// Validate transfer amount
		if (transferAmount <= 0) {
			System.out.println("CustomerServiceImpl.transferMoney()");

			return new ResponseEntity<>(new ApiResponse<>(false, "Amount must be greater than 0", null),
					HttpStatus.BAD_REQUEST);
		}

		// Validate sender account
		Optional<Account> fromAccOpt = accountrepo.findByAccountNumber(transferDTO.getFromAccount());
		if (fromAccOpt.isEmpty()) {
			return new ResponseEntity<>(new ApiResponse<>(false, "Sender account does not exist", null),
					HttpStatus.NOT_FOUND);
		}

		// Validate receiver account
		Optional<Account> toAccOpt = accountrepo.findByAccountNumber(transferDTO.getToAccount());
		if (toAccOpt.isEmpty()) {
			return new ResponseEntity<>(new ApiResponse<>(false, "Receiver account does not exist", null),
					HttpStatus.NOT_FOUND);
		}

		Account fromAccount = fromAccOpt.get();
		Account toAccount = toAccOpt.get();

		// Generate transaction reference ID
		String txnRef = UUID.randomUUID().toString();
		String userRemark = (transferDTO.getRemarks() != null && !transferDTO.getRemarks().isBlank())
				? transferDTO.getRemarks()
				: null;

		// Insufficient balance handling
		if (fromAccount.getBalance() < transferAmount) {
			saveFailedTransaction(fromAccount, toAccount, transferAmount, txnRef, userRemark);
			return new ResponseEntity<>(new ApiResponse<>(false, "Sender account has insufficient balance", null),
					HttpStatus.BAD_REQUEST);
		}

		// Deduct and add balance
		fromAccount.setBalance(fromAccount.getBalance() - transferAmount);
		toAccount.setBalance(toAccount.getBalance() + transferAmount);
		accountrepo.save(fromAccount);

		accountrepo.save(toAccount);

		// Record transactions
		saveTransaction(fromAccount, toAccount, transferAmount, txnRef, userRemark);

		// Prepare response
		TransferResponseDTO responseDTO = new TransferResponseDTO();
		responseDTO.setFromAccount(MaskAccount.maskAccount(fromAccount.getAccountNumber()));
		responseDTO.setToAccount(MaskAccount.maskAccount(toAccount.getAccountNumber()));
		responseDTO.setTransferamount(transferAmount);
		responseDTO.setSenderAvailableBalance(fromAccount.getBalance());
		responseDTO.setReferenceId(txnRef);
		responseDTO.setStatus("SUCCESS");
		responseDTO.setMessage("Transfer completed successfully");

		ApiResponse<TransferResponseDTO> response = new ApiResponse<>(true, MessageConstants.MONEY_TRANSFER_SUCCESSFULL,
				responseDTO);

		return new ResponseEntity<>(response, HttpStatus.OK);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

	// ------------------------------------------------------------------------------------------------------------
	// CHECK BALANCE
	// ------------------------------------------------------------------------------------------------------------
	@Override
	public ResponseEntity<ApiResponse<BalanceResponseDTO>> checkBalance(String accountNumber) {

		Optional<Account> byAccountNumber = accountrepo.findByAccountNumber(accountNumber);
		if (byAccountNumber.isEmpty()) {
			ApiResponse<BalanceResponseDTO> errorResponse = new ApiResponse<>(false, "Invalid account number", null);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		Account account = byAccountNumber.get();
		BalanceResponseDTO responseDTO = new BalanceResponseDTO(account.getAccountNumber(),
				account.getCustomer().getName(), account.getBalance(), "Rupay", account.getCustomer().getDob());

		ApiResponse<BalanceResponseDTO> successResponse = new ApiResponse<>(true, "Balance fetched successfully",
				responseDTO);

		return new ResponseEntity<>(successResponse, HttpStatus.OK);
	}
	
	
	

	// ------------------------------------------------------------------------------------------------------------
	// TRANSACTIONS BY MONTH RANGE (HTML type="month")
	// ------------------------------------------------------------------------------------------------------------
	// @Override
	// public List<TransactionResponseDTO> getTransactionsByMonthRange(String
	// accountNumber, String month1, String month2) {
	//
	// YearMonth startYm = YearMonth.parse(month1);
	// YearMonth endYm = YearMonth.parse(month2);
	//
	// LocalDateTime startDate = startYm.atDay(1).atStartOfDay();
	// LocalDateTime endDate = endYm.atEndOfMonth().atTime(23, 59, 59);
	//
	// List<Transaction> transactions =
	// transactionRepo.findTransactionsByAccountAndDateRange1(accountNumber,
	// startDate, endDate);
	//
	// List<TransactionResponseDTO> dtoList = transactions.stream().map(tx -> {
	// TransactionResponseDTO dto = new TransactionResponseDTO();
	// dto.setDate(tx.getTransactionTime());
	// dto.setReferenceNo(tx.getReferenceId());
	//
	// if ("FAILED".equalsIgnoreCase(tx.getStatus())) {
	// dto.setDescription("FAILED - " + tx.getTransactionType());
	// dto.setDebit(0.0);
	// dto.setCredit(0.0);
	// } else {
	// dto.setDescription(tx.getTransactionType());
	// if ("DEPOSIT".equalsIgnoreCase(tx.getTransactionType())
	// || "CREDIT".equalsIgnoreCase(tx.getTransactionType())
	// || "TRANSFER_RECEIVED".equalsIgnoreCase(tx.getTransactionType())) {
	// dto.setCredit(tx.getAmount());
	// dto.setDebit(null);
	// } else if ("DEBIT".equalsIgnoreCase(tx.getTransactionType())
	// || "WITHDRAWAL".equalsIgnoreCase(tx.getTransactionType())
	// || "TRANSFER_SENT".equalsIgnoreCase(tx.getTransactionType())) {
	// dto.setDebit(tx.getAmount());
	// dto.setCredit(null);
	// } else {
	// dto.setDebit(null);
	// dto.setCredit(null);
	// }
	// }
	// dto.setBalance(tx.getAvailableBalance());
	// return dto;
	// }).toList();
	//
	// return dtoList;
	// }
	
	
	
	
	
	

	@Override
	public List<TransactionResponseDTO> getTransactionsByMonthRange(String accountNumber, String month1,
			String month2) {

		YearMonth startYm = YearMonth.parse(month1);
		YearMonth endYm = YearMonth.parse(month2);

		LocalDateTime startDate = startYm.atDay(1).atStartOfDay();
		LocalDateTime endDate = endYm.atEndOfMonth().atTime(23, 59, 59);

		List<Transaction> transactions = transactionRepo.findTransactionsByAccountAndDateRange1(accountNumber,
				startDate, endDate);

		return transactions.stream().map(tx -> {
			TransactionResponseDTO dto = new TransactionResponseDTO();
			dto.setDate(tx.getTransactionTime());
			dto.setReferenceNo(tx.getReferenceId());
			dto.setBalance(tx.getAvailableBalance());

			if ("FAILED".equalsIgnoreCase(tx.getStatus())) {
				dto.setDescription("FAILED - " + tx.getTransactionType());
				dto.setDebit(0.0);
				dto.setCredit(0.0);
			} else {
				String description;
				switch (tx.getTransactionType().toUpperCase()) {
				case "TRANSFER_SENT":
					description = "Transfer to " + maskAccount(tx.getToAccount());
					dto.setDebit(tx.getAmount());
					dto.setCredit(null);
					break;
				case "TRANSFER_RECEIVED":
					description = "Received from " + maskAccount(tx.getFromAccount());
					dto.setCredit(tx.getAmount());
					dto.setDebit(null);
					break;
				case "DEPOSIT":
					description = "Cash Deposit";
					dto.setCredit(tx.getAmount());
					dto.setDebit(null);
					break;
				case "WITHDRAWAL":
					description = "Cash Withdrawal";
					dto.setDebit(tx.getAmount());
					dto.setCredit(null);
					break;
				default:
					description = tx.getTransactionType();
					dto.setDebit(null);
					dto.setCredit(null);
				}
				dto.setDescription(description);
			}

			return dto;
		}).toList();
	}

	// Utility method for masking account numbers
	private String maskAccount(String account) {
		if (account == null || account.length() < 4)
			return "****";
		return "****" + account.substring(account.length() - 4);
	}
	
	
	

	// ------------------------------------------------------------------------------------------------------------
	// TRANSACTIONS BY DATE RANGE (HTML type="datetime-local")
	// ------------------------------------------------------------------------------------------------------------
//	@Override
//	public List<TransactionResponseHistroyDTO> getTransactionsByAccountAndDateRange(String accountNumber,
//			LocalDateTime startDate, LocalDateTime endDate) {
//
//		List<Transaction> transactions = transactionRepo.findTransactionsByAccountAndDateRange(accountNumber, startDate,
//				endDate);
//
//		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//
//		List<TransactionResponseHistroyDTO> dtoList = new ArrayList<>();
//
//		for (Transaction txn : transactions) {
//			boolean isDebit = txn.getFromAccount().equals(accountNumber)
//					&& ("DEBIT".equalsIgnoreCase(txn.getTransactionType())
//							|| "WITHDRAWAL".equalsIgnoreCase(txn.getTransactionType()));
//			boolean isCredit = txn.getToAccount().equals(accountNumber)
//					&& ("CREDIT".equalsIgnoreCase(txn.getTransactionType())
//							|| "DEPOSIT".equalsIgnoreCase(txn.getTransactionType()));
//
//			if (isDebit || isCredit) {
//				TransactionResponseHistroyDTO dto = new TransactionResponseHistroyDTO();
//				dto.setTxnId(txn.getId());
//				dto.setFromAccount(txn.getFromAccount());
//				dto.setToAccount(txn.getToAccount());
//				dto.setType(txn.getTransactionType());
//				dto.setAmount(String.valueOf(txn.getAmount()));
//				dto.setTransactionDate(txn.getTransactionTime().format(dateFormatter));
//				dto.setTransactionTime(txn.getTransactionTime().format(timeFormatter));
//				dto.setStatus(txn.getStatus());
//				dto.setRemarks(txn.getRemarks());
//				dto.setBalance(txn.getAvailableBalance());
//				dto.setReferenceid(txn.getReferenceId());
//				dto.setDescription(txn.getDescriptioncreditanddebit());
//				dtoList.add(dto);
//			}
//		}
//
//		return dtoList;
//	}
//
//	
//	
//	
//	
//	
//	
	
	
	
	
	
	
	
	
	
	
//	-------------******ya bhi sahi ho gya ab full and final--------------------------------------------------------------
	
	
	//ye tab kam ayega jab range me nikalna ho
	
	@Override
	public List<TransactionResponseHistroyDTO> getTransactionsByAccountAndDateRange(
	        String accountNumber,
	        LocalDateTime startDate,
	        LocalDateTime endDate) {

	    System.out.println("Fetching transactions for account: " + accountNumber);
	    System.out.println("Start Date: " + startDate);
	    System.out.println("End Date: " + endDate);

	    List<Transaction> transactions = transactionRepo.findTransactionsByAccountAndDateRange(accountNumber, startDate, endDate);
	    System.out.println("list of trans: " + transactions);

	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	    List<TransactionResponseHistroyDTO> dtoList = new ArrayList<>();

	    for (Transaction txn : transactions) {
	        boolean isDebit = txn.getFromAccount().equals(accountNumber)
	                && ("DEBIT".equalsIgnoreCase(txn.getTransactionType())
	                    || "WITHDRAWAL".equalsIgnoreCase(txn.getTransactionType())
	                    || "TRANSFER_SENT".equalsIgnoreCase(txn.getTransactionType()));

	        boolean isCredit = txn.getToAccount().equals(accountNumber)
	                && ("CREDIT".equalsIgnoreCase(txn.getTransactionType())
	                    || "DEPOSIT".equalsIgnoreCase(txn.getTransactionType())
	                    || "TRANSFER_RECEIVED".equalsIgnoreCase(txn.getTransactionType()));

	        if (isDebit || isCredit) {
	            TransactionResponseHistroyDTO dto = new TransactionResponseHistroyDTO();
	            dto.setTxnId(txn.getId());
	            dto.setFromAccount(MaskAccount.maskAccount(txn.getFromAccount()));
	            dto.setToAccount(MaskAccount.maskAccount(txn.getToAccount()));
	            dto.setType(txn.getTransactionType());
	            dto.setTransactionDate(txn.getTransactionTime().format(dateFormatter));
	            dto.setTransactionTime(txn.getTransactionTime().format(timeFormatter));
	            dto.setStatus(txn.getStatus());
	            dto.setRemarks(txn.getRemarks());
	            dto.setBalance(txn.getAvailableBalance());
	            dto.setReferenceid(txn.getReferenceId());
	            dto.setDescription(txn.getDescriptioncreditanddebit());

	            // ✅ Set debit/credit amount separately
	            if (isDebit) {
	                dto.setDebitAmount(String.valueOf(txn.getAmount()));
	                dto.setCreditAmount("");
	            } else if (isCredit) {
	                dto.setCreditAmount(String.valueOf(txn.getAmount()));
	                dto.setDebitAmount("");
	            }

	            // Optional: still set generic amount if needed
	            dto.setAmount(String.valueOf(txn.getAmount()));

	            dtoList.add(dto);
	        }
	    }

	    return dtoList;
	}

	
	
	
	
	
	
	
	
	// ------------------------------------------------------------------------------------------------------------
	// ALL TRANSACTIONS FOR ACCOUNT
	// ------------------------------------------------------------------------------------------------------------
	@Override
	public List<TransactionResponseHistroyDTO> findTransactionsByAccount(String accountNumber) {

		System.out.println("CustomerServiceImpl.findTransactionsByAccount()");
		
		List<Transaction> transactions = transactionRepo.findTransactionsByAccountNumber(accountNumber.trim());
		System.out.println("transaction size:"+transactions.size());

		System.out.println("_________________");
		List<TransactionResponseHistroyDTO> dtoList = new ArrayList<>();

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		for (Transaction txn : transactions) {
			System.out.println(txn);

			boolean include = false;

			if ("DEPOSIT".equalsIgnoreCase(txn.getTransactionType()))
				include = accountNumber.equals(txn.getToAccount());

			else if ("DEBIT".equalsIgnoreCase(txn.getTransactionType()))
				include = accountNumber.equals(txn.getFromAccount());

			else if ("CREDIT".equalsIgnoreCase(txn.getTransactionType()))
				include = accountNumber.equals(txn.getToAccount());

			if (!include)
				continue;

			
			//this is for response in chrome browser
			
			TransactionResponseHistroyDTO dto = new TransactionResponseHistroyDTO();
			
			System.out.println("Transction response preparing.....");
			
			
			
			
			
			
			

			dto.setTxnId(txn.getId());
			System.out.println(dto);
//			dto.setFromAccount(MaskAccount.maskAccount(txn.getFromAccount()));
			System.out.println(dto);

			System.out.println("kiske me paia bhej rhe h:"+txn.getToAccount());

			dto.setToAccount(MaskAccount.maskAccount(txn.getToAccount()));
			dto.setFromAccount(MaskAccount.maskAccount(accountNumber));
			System.out.println(dto);
			System.out.println("************************");

			dto.setType(txn.getTransactionType());
			System.out.println(dto);


			dto.setAmount(("DEBIT".equalsIgnoreCase(txn.getTransactionType()) ? "-" : "+") + txn.getAmount());

			System.out.println(dto);

			if (txn.getTransactionTime() != null) {
				dto.setTransactionDate(txn.getTransactionTime().format(dateFormatter));
				dto.setTransactionTime(txn.getTransactionTime().format(timeFormatter));
			}
			dto.setStatus(txn.getStatus());
			System.out.println(dto);

			dto.setRemarks(txn.getRemarks());
			System.out.println(dto);

			dto.setBalance(txn.getAvailableBalance());
			System.out.println(dto);

			dto.setReferenceid(txn.getReferenceId());
			System.out.println(dto);

			dto.setDescription(txn.getDescriptioncreditanddebit());
			System.out.println(dto);

			dtoList.add(dto);
			System.out.println(dtoList);
		}

		return dtoList;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// ------------------------------------------------------------------------------------------------------------
	// HELPER METHODS
	// ------------------------------------------------------------------------------------------------------------

	// Save successful debit and credit transactions
	private void saveTransaction(Account fromAccount, Account toAccount, double amount, String txnRef,
			String userRemark) {
		Transaction debitTxn = new Transaction();
		debitTxn.setReferenceId(txnRef);
		debitTxn.setAccount(fromAccount);
		debitTxn.setFromAccount(fromAccount.getAccountNumber());
		debitTxn.setToAccount(toAccount.getAccountNumber());
		debitTxn.setTransactionType("DEBIT");
		debitTxn.setAmount(amount);
		debitTxn.setAvailableBalance(fromAccount.getBalance());
		debitTxn.setChannel("ONLINE");
		debitTxn.setInitiatedBy(fromAccount.getCustomer().getName());
		debitTxn.setRemarks(userRemark != null ? userRemark
				: "Transferred to " + MaskAccount.maskAccount(toAccount.getAccountNumber()));
		debitTxn.setStatus("SUCCESS");
		debitTxn.setTransactionTime(LocalDateTime.now());
		debitTxn.setDescriptioncreditanddebit("Money Transfered..");
		transactionRepo.save(debitTxn);

		Transaction creditTxn = new Transaction();
		creditTxn.setReferenceId(txnRef);
		creditTxn.setAccount(toAccount);
		creditTxn.setFromAccount(fromAccount.getAccountNumber());
		creditTxn.setToAccount(toAccount.getAccountNumber());
		creditTxn.setTransactionType("CREDIT");
		creditTxn.setAmount(amount);
		creditTxn.setAvailableBalance(toAccount.getBalance());
		creditTxn.setChannel("ONLINE");
		creditTxn.setInitiatedBy(fromAccount.getCustomer().getName());
		creditTxn.setRemarks(userRemark != null ? userRemark
				: "Received from " + MaskAccount.maskAccount(fromAccount.getAccountNumber()));
		creditTxn.setStatus("SUCCESS");
		creditTxn.setTransactionTime(LocalDateTime.now());
		creditTxn.setDescriptioncreditanddebit("Money Received..");
		transactionRepo.save(creditTxn);
	}

	// Save failed transactions for insufficient balance
	private void saveFailedTransaction(Account fromAccount, Account toAccount, double amount, String txnRef,
			String userRemark) {
		Transaction failedDebit = new Transaction();
		failedDebit.setReferenceId(txnRef);
		failedDebit.setAccount(fromAccount);
		failedDebit.setFromAccount(fromAccount.getAccountNumber());
		failedDebit.setToAccount(toAccount.getAccountNumber());
		failedDebit.setTransactionType("DEBIT");
		failedDebit.setAmount(amount);
		failedDebit.setAvailableBalance(fromAccount.getBalance());
		failedDebit.setChannel("ONLINE");
		failedDebit.setInitiatedBy(fromAccount.getCustomer().getName());
		failedDebit.setRemarks(userRemark != null ? userRemark : "Failed transfer to " + toAccount.getAccountNumber());
		failedDebit.setStatus("FAILED");
		failedDebit.setTransactionTime(LocalDateTime.now());
		failedDebit.setDescriptioncreditanddebit("Failed Becouse Insufficient Balance..");
		transactionRepo.save(failedDebit);

		Transaction failedCredit = new Transaction();
		failedCredit.setReferenceId(txnRef);
		failedCredit.setAccount(toAccount);
		failedCredit.setFromAccount(fromAccount.getAccountNumber());
		failedCredit.setToAccount(toAccount.getAccountNumber());
		failedCredit.setTransactionType("CREDIT");
		failedCredit.setAmount(amount);
		failedCredit.setAvailableBalance(toAccount.getBalance());
		failedCredit.setChannel("ONLINE");
		failedCredit.setInitiatedBy(fromAccount.getCustomer().getName());
		failedCredit
				.setRemarks(userRemark != null ? userRemark : "Failed transfer from " + fromAccount.getAccountNumber());
		failedCredit.setStatus("FAILED");
		failedCredit.setTransactionTime(LocalDateTime.now());
		failedCredit.setDescriptioncreditanddebit("Money Not Receeiver Becouse Sender Does not Have Balance.");
		transactionRepo.save(failedCredit);
	}
}
