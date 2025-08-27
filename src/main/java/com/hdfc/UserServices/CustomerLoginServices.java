package com.hdfc.UserServices;

import org.springframework.http.ResponseEntity;

import com.hdfc.ApiResponse.ApiResponse;
import com.hdfc.DTO.CustomerLoginResponse;

public interface CustomerLoginServices {
	public ResponseEntity<ApiResponse<CustomerLoginResponse>> loginCustomer(String customerId, String password);
	

}
