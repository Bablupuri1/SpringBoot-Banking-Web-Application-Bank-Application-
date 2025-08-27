package com.hdfc.Utils;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdfc.Repositories.CustomerRepository;

@Component
public class GeneratorUtil {

    @Autowired
    private CustomerRepository customerRepository;

    private static final AtomicLong custIdCounter = new AtomicLong(1000); // starts from 1000

    // Generates unique customerId like: CUST1634578901234
    public String generateUniqueCustomerId() {
        String customerId;
        do {
            customerId = "CUST" + System.currentTimeMillis() + custIdCounter.getAndIncrement();
        } while (customerRepository.existsByCustomerId(customerId));
        return customerId;
    }

    // Generates unique password like: HDFC@123456
    public String generateUniquePassword() {
        String password;
        do {
            password = "HDFC@" + (100000 + new Random().nextInt(900000)); // 6-digit random
        } while (customerRepository.existsByPassword(password));
        return password;
    }

    // Generates accountNumber like: 5240 + timestamp + 5-digit random = 16 digits
    public String generateAccountNumber() {
        String prefix = "5240"; // 4 digits (Bank prefix)

        // Take currentTimeMillis and pad to fixed length (8 digits)
        String time = String.valueOf(System.currentTimeMillis() % 100000000L);
        time = String.format("%08d", Long.parseLong(time)); // always 8 digits

        // Random 4 digit number
        int random = new Random().nextInt(9000) + 1000; // 1000-9999

        return prefix + time + random; // total 16 digits
    }

}
