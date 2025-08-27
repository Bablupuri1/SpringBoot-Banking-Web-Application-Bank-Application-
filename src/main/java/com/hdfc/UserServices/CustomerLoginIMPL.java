package com.hdfc.UserServices;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hdfc.ApiResponse.ApiResponse;
import com.hdfc.DTO.CustomerLoginResponse;
import com.hdfc.Model.Account;
import com.hdfc.Model.Customer;
import com.hdfc.Repositories.CustomerRepository;

@Service
public class CustomerLoginIMPL implements CustomerLoginServices {

	@Autowired
	private CustomerRepository customerRepo;

	@Override
	public ResponseEntity<ApiResponse<CustomerLoginResponse>> loginCustomer(String customerId, String password) {

	    Optional<Customer> optionalCustomer = customerRepo.findByCustomerId(customerId);
	    if (optionalCustomer.isEmpty()) {
	        ApiResponse<CustomerLoginResponse> response = new ApiResponse<>(
	                false, "Customer Id Not Matched..", null);
	        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	    }

	    Customer customer = optionalCustomer.get();

	    if (!customer.getPassword().equals(password)) {
	        ApiResponse<CustomerLoginResponse> response = new ApiResponse<>(
	                false, "Password Not Matched..", null);
	        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	    }

	    //  Fetch Account
	    Account account = customer.getAccount();

	    // Create Login Response
	    CustomerLoginResponse loginResponse = new CustomerLoginResponse();
	    loginResponse.setMessage("Login Successful");
	    loginResponse.setCustomerName(customer.getName());
	    loginResponse.setRole(customer.getRole());

	    if (account != null) {
	        loginResponse.setAccountNumber(account.getAccountNumber());
	        loginResponse.setBalance(account.getBalance());
	    }

	    ApiResponse<CustomerLoginResponse> apiresponse = new ApiResponse<>(
	            true, "Login Successfully..", loginResponse);

	    return new ResponseEntity<>(apiresponse, HttpStatus.OK);
	}

}
