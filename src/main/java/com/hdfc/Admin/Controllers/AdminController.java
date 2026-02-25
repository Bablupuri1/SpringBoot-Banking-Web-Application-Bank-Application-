package com.hdfc.Admin.Controllers;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@CrossOrigin(origins = "https://spring-boot-banking-frontend.vercel.app/") 
// Allow frontend hosted on Vercel to access these APIs (CORS configuration)
public class AdminController {

    @Autowired
    private AdminService adminService; // Service layer handling business logic

    // ------------------- Account Management -------------------

    @PostMapping("/create-account")
    public ResponseEntity<ApiResponse<CustomerResponseCredentialDTO>> createAccount(
            @RequestBody CustomerAccountDTO requestDto) {
        // Create a new customer account
        return adminService.createAccount(requestDto);
    }

    // ------------------- Deposit Money -------------------

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<DepositResponseDTO>> deposit(@RequestBody DepositRequestDTO request) {
        // Deposit money into an account
        System.out.println("AdminController.deposit()");
        return adminService.depositToAccount(request);
    }

    // ------------------- Withdraw Money -------------------

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<WithdrawResponseDTO>> withdrawFromAccount(@RequestBody WithdrawRequestDTO request) {
        // Withdraw money from an account
        System.out.println("AdminController.withdrawFromAccount()");
        return adminService.withdrawFromAccount(request);
    }

    // ------------------- Transfer Money -------------------

    @PostMapping("/transferBetweenAccount")
    public ResponseEntity<ApiResponse<TransferResponseDTO>> transferMoney(@RequestBody TransferRequestDTO transferDTO) {
        // Transfer money between two accounts
        System.out.println("CustomerControllers.transferMoney()");
        System.out.println("Transfer details: " + transferDTO.toString());
        return adminService.transferMoney(transferDTO);
    }

    // ------------------- Transactions by Date Range -------------------

    @GetMapping("/getTransactionAccountByRange")
    public List<MiniStatementDTO> getTransactionsByAccountAndDateRange(String accountNumber, String startDate,
            String endDate) {
        // Fetch transactions for a given account between two date-time ranges
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime localDateTime1 = LocalDateTime.parse(startDate, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(endDate, formatter);

        List<MiniStatementDTO> transactions = adminService
                .getTransactionsByAccountAndDateRange(accountNumber, localDateTime1, localDateTime2);

        transactions.forEach(System.out::println);
        return transactions;
    }

    // ------------------- Transactions by Month Range -------------------

    @GetMapping("/getTransactionMonth")
    @ResponseBody
    public List<TransactionResponseDTO> getTransactionsByMonthRange(@RequestParam String accountNumber,
            @RequestParam String month1, @RequestParam String month2) {
        // Fetch transactions for a given account between two months
        return adminService.getTransactionsByMonthRange(accountNumber, month1, month2);
    }

    // ------------------- Paginated Accounts -------------------

    @GetMapping("/getAllAccount")
    public AccountPageResponse getPaginatedAccounts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // Fetch all accounts with pagination
        Page<Account> allAccounts = adminService.getAllAccounts(page, size);
        List<AccountDTO> accountDTOs = new ArrayList<>();

        for (Account account : allAccounts.getContent()) {
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

    // ------------------- Paginated Customers -------------------

    @GetMapping("/getAllCustomers")
    public CustomersPageResponse getAllCustomersWithPagination(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // Fetch all customers with pagination
        Page<Customer> customerPage = adminService.getAllCustomers(page, size);
        List<CustomerDTO> customerDTOs = new ArrayList<>();

        for (Customer customer : customerPage.getContent()) {
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

    // ------------------- All Transactions with Filters -------------------

    @GetMapping("/getAllTransactions")
    public ResponseEntity<PagedResponse<Transaction>> getTransactions(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String status,
            @RequestParam(required = false) String type, @RequestParam(required = false) String accountId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        // Fetch all transactions with optional filters (status, type, account, date range)
        return ResponseEntity.ok(adminService.getTransactions(page, size, status, type, accountId, fromDate, toDate));
    }

    // ------------------- Recent Transactions -------------------

    @GetMapping("/RecentTransaction")
    public ResponseEntity<List<TransactionDto>> getRecentTransactions() {
        // Fetch most recent transactions
        List<TransactionDto> transactions = adminService.fetchRecentTransactions();
        return ResponseEntity.ok(transactions);
    }

    // ------------------- Count APIs -------------------

    @GetMapping("/getnoofAccounts")
    public ResponseEntity<?> getnoofAccounts() {
        // Get total number of accounts
        long countAccounts = adminService.countAccounts();
        return new ResponseEntity<Long>(countAccounts, HttpStatus.OK);
    }

    @GetMapping("/getnoofCustomers")
    public ResponseEntity<?> getnoofCustomers() {
        // Get total number of customers
        long countCustomers = adminService.countCustomers();
        return new ResponseEntity<Long>(countCustomers, HttpStatus.OK);
    }

    @GetMapping("/getnoofTransactions")
    public ResponseEntity<?> getnoofTransactions() {
        // Get total number of transactions
        long countTransactions = adminService.countTransactions();
        return new ResponseEntity<Long>(countTransactions, HttpStatus.OK);
    }
}
