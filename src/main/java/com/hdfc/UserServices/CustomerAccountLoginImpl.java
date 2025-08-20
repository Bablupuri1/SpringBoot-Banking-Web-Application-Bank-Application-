package com.hdfc.UserServices;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hdfc.ApiResponse.ApiResponse;
import com.hdfc.DTO.CustomerLoginResponse;
import com.hdfc.Model.Customer;
import com.hdfc.Repositories.CustomerRepository;

@Service
public class CustomerAccountLoginImpl implements CustomerLoginServices {

	@Autowired
	private CustomerRepository customerRepo;

	@Override
	public ResponseEntity<ApiResponse<CustomerLoginResponse>> loginCustomer(String customerId, String password) {

		// Step 1: Check if account exists
		Optional<Customer> optionalCustomer = customerRepo.findByCustomerId(customerId);
		if (optionalCustomer.isEmpty()) {

			// return new ResponseEntity<>(response, HttpStatus.OK);

			ApiResponse<CustomerLoginResponse> response = new ApiResponse<CustomerLoginResponse>(false,
					"Customer Id Not Matched..", null);

			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

		}

		Customer customer = optionalCustomer.get();

		// Step 2: Simple password match (no encryption)
		if (!customer.getPassword().equals(password)) {

			ApiResponse<CustomerLoginResponse> response = new ApiResponse<CustomerLoginResponse>(false,
					"Password Not Matched..", null);
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

		}

		// Step 3: Create Login Response
		CustomerLoginResponse loginResponse = new CustomerLoginResponse();
		loginResponse.setMessage("Login Successful");
		loginResponse.setCustomerName(customer.getName());
		loginResponse.setRole(customer.getRole());

		// we need to seprate ResponseApi then return in ResponseEntity

		ApiResponse<CustomerLoginResponse> apiresponse = new ApiResponse<CustomerLoginResponse>(true,
				"Login Successfully..", loginResponse);

		// Step 4: Return success response
		return new ResponseEntity<>(apiresponse, HttpStatus.OK);

	}
}
