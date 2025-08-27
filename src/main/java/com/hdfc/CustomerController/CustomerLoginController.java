package com.hdfc.CustomerController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hdfc.ApiResponse.ApiResponse;
import com.hdfc.DTO.CustomerLoginRequest;
import com.hdfc.DTO.CustomerLoginResponse;
import com.hdfc.UserServices.CustomerLoginServices;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:5173")  //  Allow React app
public class CustomerLoginController {

    @Autowired
    private CustomerLoginServices loginService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<CustomerLoginResponse>> loginCustomer(
            @RequestBody CustomerLoginRequest loginRequest) {

        return loginService.loginCustomer(loginRequest.getCustomerId(), loginRequest.getPassword());
    }
}
