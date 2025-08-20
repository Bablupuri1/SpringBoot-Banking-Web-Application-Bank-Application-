package com.hdfc.Repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hdfc.Model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	
	//these all  are cutome method
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByCustomerId(String customerId);

    //  New method to fetch customer by customerId
    Optional<Customer> findByCustomerId(String customerId);
}
