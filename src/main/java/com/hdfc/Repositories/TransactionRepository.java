package com.hdfc.Repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hdfc.Model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	  // Custom query method to fetch transactions between two dates for a specific account
//    List<TransactionResponseDTO> findByAccountIdAndDateBetween(Long accountId, LocalDate startDate, LocalDate endDate);
    
    
	
	
    
	
	
//    List<Transaction> findByAccount_IdAndTransactionTimeBetween(Long accountId, LocalDate startDate, LocalDate endDate);
	
	
	  @Query(" SELECT t FROM Transaction t " 
	          + "WHERE t.account.accountNumber = :accountNumber " + 
			  "AND t.transactionTime BETWEEN :startDate AND :endDate"
			  )
	    List<Transaction> findTransactionsByAccountAndDateRange(
	            @Param("accountNumber") String accountNumber,
	            @Param("startDate") LocalDateTime startDate,
	            @Param("endDate") LocalDateTime endDate
	    );
	  
	  
	  
	  
	  
	  
//	  This is  equivalant to jpql
	  
	  
	  
	  
//	  @Query(value = "SELECT t.* " +
//              "FROM transactions t " +
//              "JOIN accounts a ON t.account_id = a.id " +
//              "WHERE a.account_number = :accountNumber " +
//              "AND t.transaction_time BETWEEN :startDate AND :endDate",
//      nativeQuery = true)
//List<Transaction> findTransactionsByAccountAndDateRange(
//       @Param("accountNumber") String accountNumber,
//       @Param("startDate") LocalDateTime startDate,
//       @Param("endDate") LocalDateTime endDate
//);

	  

}
