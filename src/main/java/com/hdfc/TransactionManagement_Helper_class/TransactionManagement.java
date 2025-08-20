package com.hdfc.TransactionManagement_Helper_class;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdfc.Model.Account;
import com.hdfc.Model.Transaction;
import com.hdfc.Repositories.AccountRepository;
import com.hdfc.Repositories.TransactionRepository;



@Component
public class TransactionManagement {
	
	
	 @Autowired
	    private TransactionRepository transactionRepo;
	 
	 @Autowired
	 private AccountRepository accountrepo;
	
	public void recordTransaction(Account account, String type, double amount, String remarks, String initiatedBy) {
	    Transaction transaction = new Transaction();
	    transaction.setAccount(account);
	    transaction.setFromAccount("ADMIN101");  // yeh bhi constant me nikal sakte ho
	    transaction.setToAccount(account.getAccountNumber());
	    transaction.setTransactionType(type);
	    transaction.setAmount(amount);
	    transaction.setAvailableBalance(account.getBalance());
	    transaction.setChannel("BRANCH");
	    transaction.setInitiatedBy(initiatedBy);
	    transaction.setRemarks(remarks);
	    transaction.setStatus("SUCCESS");
	    transaction.setTransactionTime(LocalDateTime.now());

	    transactionRepo.save(transaction);
//	    accountrepo
	    Account ac= account;
	    
	    
	    ArrayList<Transaction> list= new ArrayList<Transaction>();
	    
	    
	    list.add(transaction);
	    ac.setTransactions(list);
	    
	    accountrepo.save(ac);
	    
	    
	    
	    
	}

	
	

}
