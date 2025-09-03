package com.hdfc.Repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hdfc.Model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	
	
	
	
// THIS IS USED FOR GETTING ALL TRANSACTION  BETWEEN RANGE_________________________________________________
	//format
	//2025-08Ihtml input type="month"
	
	
	@Query(" SELECT t FROM Transaction t " + "WHERE t.account.accountNumber = :accountNumber "
			+ "AND t.transactionTime BETWEEN :startDate AND :endDate")
	List<Transaction> findTransactionsByAccountAndDateRange1(@Param("accountNumber") String accountNumber,
			@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
	
	
	
	

	
//_____________________________________________________________________________________________
	
	
	
	
	//
	// // accountNumber + transaction_time between range
	// @Query("SELECT t FROM Transaction t WHERE t.accountNumber = :accountNumber "
	// +
	// "AND t.transactionTime BETWEEN :startDate AND :endDate")
	// List<Transaction> findTransactionsByAccountAndDateRange(
	// @Param("accountNumber") String accountNumber,
	// @Param("startDate") LocalDateTime startDate,
	// @Param("endDate") LocalDateTime endDate);
	//

	// This is equivalant to jpql

	// @Query(value = "SELECT t.* " +
	// "FROM transactions t " +
	// "JOIN accounts a ON t.account_id = a.id " +
	// "WHERE a.account_number = :accountNumber " +
	// "AND t.transaction_time BETWEEN :startDate AND :endDate",
	// nativeQuery = true)
	// List<Transaction> findTransactionsByAccountAndDateRange(
	// @Param("accountNumber") String accountNumber,
	// @Param("startDate") LocalDateTime startDate,
	// @Param("endDate") LocalDateTime endDate
	// );

	List<Transaction> findByTransactionTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

	// find transactions by account number + date range
//	List<Transaction> findByFromAccountOrToAccountAndTransactionTimeBetween(String fromAccount,  String toAccount, LocalDateTime startDate,
//			LocalDateTime endDate);
//	
//	
//	
	
	
	
//	-------------------THIS IS USEFUL WHEN WE WANT TO  SEE THE  TRANSACTION BETWEEN TWO GIVEN RANGE DATE AND TIME-------------------
	
	@Query("SELECT t FROM Transaction t " +
		       "WHERE (t.fromAccount = :account OR t.toAccount = :account) " +
		       "AND t.transactionTime >= :startDate " +
		       "AND t.transactionTime <= :endDate")
		List<Transaction> findTransactionsByAccountAndDateRange(
		        @Param("account") String account,
		        @Param("startDate") LocalDateTime startDate,
		        @Param("endDate") LocalDateTime endDate);
	
	

	
	
//	_________________________________________________________________________________________________
	
	
	

//	______________________________testing purpose full history_________________________________________________
	
	
	
	
	@Query("SELECT t FROM Transaction t " +
		       "WHERE t.fromAccount = :accountNumber OR t.toAccount = :accountNumber " +
		       "ORDER BY t.transactionTime DESC")
		List<Transaction> findTransactionsByAccountNumber(@Param("accountNumber") String accountNumber);
	
	
//	______________________________________________________________________________
	
	
	 List<Transaction> findByAccountId(Long accountId);
	 
	 
	 
	 
	 
	 
	 
	 ///this is useful for get the latesg 0 transaction
	
	

	    @Query("SELECT t FROM Transaction t WHERE t.transactionTime IS NOT NULL ORDER BY t.transactionTime DESC")
	    List<Transaction> fetchRecentTransactions();

	

}
