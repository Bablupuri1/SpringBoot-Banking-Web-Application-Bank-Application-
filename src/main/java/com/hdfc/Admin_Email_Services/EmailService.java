package com.hdfc.Admin_Email_Services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Existing method: account creation email
    public void sendUserCredentials
    (   String toEmail, 
    		String customerName,
    		String customerId,
        String accountNumber,
        String password ,
        double depositAmmmount,
        double UpdatedBalance,
        boolean isopened
        ) 
    
    {
    	
    	
    	
        sendEmail(toEmail, customerName, customerId, accountNumber, password, depositAmmmount, UpdatedBalance, false);
        
        
        
    }

    // Overloaded method: deposit email
    public void sendUserCredentials(String toEmail, 
    		String customerName,
    		String customerId,
                                    String accountNumber,
                                    double depositedAmount, double newBalance) {
        sendEmail(toEmail, customerName, customerId, accountNumber, null, depositedAmount, newBalance, true);
    }

    
    // Private helper method to avoid code duplication
    private void sendEmail(
    		String toEmail,
    		String customerName,
    		String customerId, 
    		String accountNumber,
         String password,
        double depositedAmount, 
        double newBalance, 
        boolean isDeposit
        
    		) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject(isDeposit ? "🏦 Deposit Successful - HDFC Bank" : "🏦 Welcome to HDFC Bank - Your Account Details");
            helper.setSentDate(new Date());

            String htmlMsg = "<html><body>"
                    + "<div style='text-align:center;'>"
                    + "<img src='cid:hdfcLogo' alt='HDFC Bank Logo' width='150'/><br/><br/>"
                    + "<h2>" + (isDeposit ? "Deposit Successful!" : "Welcome to HDFC Bank!") + "</h2>"
                    + "</div>"
                    + "<p>Hi <b>" + customerName + "</b>,</p>";

            if (isDeposit) {
                htmlMsg += "<p>Your deposit has been successfully processed. Details:</p>"
                        + "<table style='border-collapse: collapse;'>"
                        + "<tr><td style='padding:5px; font-weight:bold;'>Customer ID:</td><td>" + customerId + "</td></tr>"
                        + "<tr><td style='padding:5px; font-weight:bold;'>Account Number:</td><td>" + accountNumber + "</td></tr>"
                        + "<tr><td style='padding:5px; font-weight:bold;'>Deposited Amount:</td><td>" + depositedAmount + "</td></tr>"
                        + "<tr><td style='padding:5px; font-weight:bold;'>New Balance:</td><td>" + newBalance + "</td></tr>"
                        + "</table>";
            } else {
                htmlMsg += "<p>Your account has been created successfully. Here are your credentials:</p>"
                        + "<table style='border-collapse: collapse;'>"
                        + "<tr><td style='padding:5px; font-weight:bold;'>Customer ID:</td><td>" + customerId + "</td></tr>"
                        + "<tr><td style='padding:5px; font-weight:bold;'>Account Number:</td><td>" + accountNumber + "</td></tr>"
                        + "<tr><td style='padding:5px; font-weight:bold;'>Password:</td><td>" + password + "</td></tr>"
                        + "</table>";
            }

            htmlMsg += "<p>Please keep this information secure.</p>"
                    + "<p>Regards,<br/>HDFC Bank Team</p>"
                    + "</body></html>";

            helper.setText(htmlMsg, true);

            // Attach logo
            FileSystemResource res = new FileSystemResource("C:\\Users\\Hp\\Downloads\\LOGO FOR EMAIL\\LOGO.png");
            helper.addInline("hdfcLogo", res);

            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
