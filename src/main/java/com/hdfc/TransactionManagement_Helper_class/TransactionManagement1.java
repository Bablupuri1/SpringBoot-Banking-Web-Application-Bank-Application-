package com.hdfc.TransactionManagement_Helper_class;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdfc.Model.Account;
import com.hdfc.Model.Transaction;
import com.hdfc.Repositories.TransactionRepository;
import com.hdfc.Utils.MaskAccount;

@Component
public class TransactionManagement1 {

    @Autowired
    private TransactionRepository transactionRepo;

    // ✅ Save successful transaction (works for both user & admin)
    public void saveTransaction(Account fromAccount, Account toAccount, double amount,
                                String txnRef, String userRemark, String initiatedBy) {
        
        String debitRemarks = userRemark != null ? userRemark :
                "Transferred to " + MaskAccount.maskAccount(toAccount.getAccountNumber());

        String creditRemarks = userRemark != null ? userRemark :
                "Received from " + MaskAccount.maskAccount(fromAccount.getAccountNumber());

        // Debit Transaction
        Transaction debitTxn = new Transaction();
        debitTxn.setReferenceId(txnRef);
        debitTxn.setAccount(fromAccount);
        debitTxn.setFromAccount(fromAccount.getAccountNumber());
        debitTxn.setToAccount(toAccount.getAccountNumber());
        debitTxn.setTransactionType("DEBIT");
        debitTxn.setAmount(amount);
        debitTxn.setAvailableBalance(fromAccount.getBalance());
        debitTxn.setChannel("ONLINE");
        debitTxn.setInitiatedBy(initiatedBy);
        debitTxn.setRemarks(debitRemarks);
        debitTxn.setStatus("SUCCESS");
        debitTxn.setTransactionTime(LocalDateTime.now());
        debitTxn.setDescriptioncreditanddebit("Money Transferred.");
        transactionRepo.save(debitTxn);

        // Credit Transaction
        Transaction creditTxn = new Transaction();
        creditTxn.setReferenceId(txnRef);
        creditTxn.setAccount(toAccount);
        creditTxn.setFromAccount(fromAccount.getAccountNumber());
        creditTxn.setToAccount(toAccount.getAccountNumber());
        creditTxn.setTransactionType("CREDIT");
        creditTxn.setAmount(amount);
        creditTxn.setAvailableBalance(toAccount.getBalance());
        creditTxn.setChannel("ONLINE");
        creditTxn.setInitiatedBy(initiatedBy);
        creditTxn.setRemarks(creditRemarks);
        creditTxn.setStatus("SUCCESS");
        creditTxn.setTransactionTime(LocalDateTime.now());
        creditTxn.setDescriptioncreditanddebit("Money Received.");
        transactionRepo.save(creditTxn);
    }

    // ✅ Save failed transaction (insufficient balance or error)
    public void saveFailedTransaction(Account fromAccount, Account toAccount, double amount,
                                      String txnRef, String userRemark, String initiatedBy) {
        
        String failedDebitRemarks = userRemark != null ? userRemark :
                "Failed transfer to " + toAccount.getAccountNumber();

        String failedCreditRemarks = userRemark != null ? userRemark :
                "Failed transfer from " + fromAccount.getAccountNumber();

        // Failed Debit Transaction
        Transaction failedDebit = new Transaction();
        failedDebit.setReferenceId(txnRef);
        failedDebit.setAccount(fromAccount);
        failedDebit.setFromAccount(fromAccount.getAccountNumber());
        failedDebit.setToAccount(toAccount.getAccountNumber());
        failedDebit.setTransactionType("DEBIT");
        failedDebit.setAmount(amount);
        failedDebit.setAvailableBalance(fromAccount.getBalance());
        failedDebit.setChannel("ONLINE");
        failedDebit.setInitiatedBy(initiatedBy);
        failedDebit.setRemarks(failedDebitRemarks);
        failedDebit.setStatus("FAILED");
        failedDebit.setTransactionTime(LocalDateTime.now());
        failedDebit.setDescriptioncreditanddebit("Failed because Insufficient Balance.");
        transactionRepo.save(failedDebit);

        // Failed Credit Transaction
        Transaction failedCredit = new Transaction();
        failedCredit.setReferenceId(txnRef);
        failedCredit.setAccount(toAccount);
        failedCredit.setFromAccount(fromAccount.getAccountNumber());
        failedCredit.setToAccount(toAccount.getAccountNumber());
        failedCredit.setTransactionType("CREDIT");
        failedCredit.setAmount(amount);
        failedCredit.setAvailableBalance(toAccount.getBalance());
        failedCredit.setChannel("ONLINE");
        failedCredit.setInitiatedBy(initiatedBy);
        failedCredit.setRemarks(failedCreditRemarks);
        failedCredit.setStatus("FAILED");
        failedCredit.setTransactionTime(LocalDateTime.now());
        failedCredit.setDescriptioncreditanddebit("Money not received because sender has insufficient balance.");
        transactionRepo.save(failedCredit);
    }
}
