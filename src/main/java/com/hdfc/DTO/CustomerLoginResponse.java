package com.hdfc.DTO;

import lombok.Data;

@Data
public class CustomerLoginResponse
{
	
	  private String message;
	    private String customerName;
	    private String role;
//	    private String token; // optional used  with jwt token
	    
	    
	    
}
