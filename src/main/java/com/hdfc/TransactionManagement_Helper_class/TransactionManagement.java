package com.hdfc.TransactionManagement_Helper_class;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hdfc.Model.Account;
import com.hdfc.Model.Transaction;
import com.hdfc.Repositories.AccountRepository;
import com.hdfc.Repositories.TransactionRepository;

/**
 * TransactionManagement class is responsible for handling all operations
 * related to transactions, such as recording deposits, withdrawals, or
 * transfers.
 *
 * This class ensures that transactions are saved atomically and account
 * transaction history is updated correctly.
 */
@Component
public class TransactionManagement {

	// Constants for admin account and default channel
	private static final String ADMIN_ACCOUNT = "ADMIN52400000000";
	private static final String DEFAULT_CHANNEL = "BRANCH";

	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private AccountRepository accountRepo;

	/**
	 * Records a transaction for a given account.
	 *
	 * @param account     the account involved in the transaction
	 * @param type        type of transaction (e.g., DEPOSIT, WITHDRAW)
	 * @param amount      transaction amount
	 * @param remarks     remarks for the transaction
	 * @param initiatedBy user who initiated the transaction
	 * 
	 */
	@Transactional // Ensures atomic DB operation
	public void recordTransaction(Account account,
			String type, 
			double amount, 
			String remarks,
			String initiatedBy,String description) {
		// Basic validation to avoid invalid transactions
		if (account == null) {
			throw new IllegalArgumentException("Account cannot be null");
		}
		if (amount <= 0) {
			throw new IllegalArgumentException("Amount must be greater than zero");
		}

		// Create new transaction object
		Transaction transaction = new Transaction();
		transaction.setAccount(account);
		transaction.setFromAccount(ADMIN_ACCOUNT); // Using constant for admin
		transaction.setToAccount(account.getAccountNumber());
		transaction.setTransactionType(type);
		transaction.setAmount(amount);
		transaction.setAvailableBalance(account.getBalance());
		transaction.setChannel(DEFAULT_CHANNEL); // Using constant for channel
		transaction.setInitiatedBy(initiatedBy);
		transaction.setRemarks(remarks);
		transaction.setStatus("SUCCESS");
		transaction.setTransactionTime(LocalDateTime.now());
		transaction.setDescriptioncreditanddebit(description);

		// Save transaction in the database
		transactionRepo.save(transaction);

		// Fetch existing transaction list or create a new one
		List<Transaction> transactionList = account.getTransactions();
		if (transactionList == null) {
			transactionList = new ArrayList<>();
		}
		// Add the newly created transaction
		transactionList.add(transaction);
		account.setTransactions(transactionList);

		// Update account with new transaction list
		accountRepo.save(account);
	}




	@Transactional
	public Transaction
	recordTransaction1(
			Account account,
			String fromAccount,
			String toAccount,
			String transactionType,
			double amount,
			double AvailableBalance,
			String channel,
			String initiatedBy,
			String remarks,
			  String status,
			  LocalDateTime dateoftransaction,
			  String  description
			  ) {

		Transaction txn = new Transaction();
		txn.setAccount(account);//1
		txn.setFromAccount(fromAccount);//2
		txn.setToAccount(toAccount);//3
		txn.setTransactionType(transactionType); // assuming DEBIT for failed withdrawal4
		txn.setAmount(amount);//5
		txn.setAvailableBalance(account.getBalance()); // Balance not deducted6
		txn.setChannel(channel);//7
		txn.setInitiatedBy(initiatedBy);//8
		txn.setRemarks(remarks);//9
		txn.setStatus(status);//10
		txn.setTransactionTime(dateoftransaction);//11
		txn.setDescriptioncreditanddebit(description);

		transactionRepo.save(txn);

		// Fetch existing transaction list or create a new one
		List<Transaction> transactionList = account.getTransactions();
		if (transactionList == null) {
			transactionList = new ArrayList<>();
		}
		// Add the newly created transaction
		transactionList.add(txn);
		account.setTransactions(transactionList);

		// Update account with new transaction list
		accountRepo.save(account);


		return txn;
	}
	
	
	
	
	

}
