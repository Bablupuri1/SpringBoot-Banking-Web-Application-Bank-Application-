package com.hdfc.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hdfc.Model.Customer;



@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByCustomerId(String customerId);

    Optional<Customer> findByCustomerId(String customerId);

    boolean existsByPassword(String password);

    //  For login check
    boolean existsByCustomerIdAndPassword(String customerId, String password);

    //  If you also want full Customer details on successful login
    Optional<Customer> findByCustomerIdAndPassword(String customerId, String password);
}
