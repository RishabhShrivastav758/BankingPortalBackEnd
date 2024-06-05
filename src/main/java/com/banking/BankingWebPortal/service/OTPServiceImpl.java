package com.banking.BankingWebPortal.service;

import com.banking.BankingWebPortal.entity.OtpInfo;
import com.banking.BankingWebPortal.entity.User;
import com.banking.BankingWebPortal.exception.AccountDoesNotExists;
import com.banking.BankingWebPortal.exception.InvalidOtpException;
import com.banking.BankingWebPortal.exception.OtpRetryLimitExceededException;
import com.banking.BankingWebPortal.repository.otpInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.cache.Cache;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class OTPServiceImpl implements OTPService{

    private static final int MAX_OTP_ATTEMPTS = 3;
    private static final int MAX_OTP_ATTEMPTS_WINDOW_MINUTES = 15;
    private static final int OTP_EXPIRY_MINUTES = 5;

    @Autowired
    private EmailService emailService;

    @Autowired
    private otpInfoRepository otpInfoRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public String generateOTP(String accountNumber) {
        User user = userService.getUserByAccountNumber(accountNumber);
        if (user == null) {
            // If Invalid Account number
            throw new AccountDoesNotExists("Invalid Account Number");
        }

        OtpInfo existingOtpInfo = otpInfoRepository.findByAccountNumber(accountNumber);

        if (existingOtpInfo != null) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lastRequestTime = existingOtpInfo.getGeneratedAt();

            if (getOtpAttempts(accountNumber) >= MAX_OTP_ATTEMPTS) {
                if (lastRequestTime.isAfter(now.minusMinutes(MAX_OTP_ATTEMPTS_WINDOW_MINUTES))) {
                    // MAX_OTP_ATTEMPTS exceeds under MAX_OTP_ATTEMPTS_WINDOW_MINUTES
                    throw new OtpRetryLimitExceededException(
                            "OTP generation limit exceeded. Please try again after some time.");

                } else {
                    // MAX_OTP_ATTEMPTS exceeds but MAX_OTP_ATTEMPTS_WINDOW_MINUTES TIme Over
                    // so reset count and user can get new OTP
                    resetOtpAttempts(accountNumber);
                }
            }

        }

        String otp = null;
        if (existingOtpInfo != null) {
            if (isOtpExpired(existingOtpInfo.getGeneratedAt())) {
                otpInfoRepository.delete(existingOtpInfo);
                otp = generateNewOTP(accountNumber);
            } else {
                // OTP is valid , return same OTP but reset time
                existingOtpInfo.setGeneratedAt(LocalDateTime.now());
                otp = existingOtpInfo.getOtp();
            }
        } else {
            otp = generateNewOTP(accountNumber);
        }

        // Increment OTP request count for the user
        incrementOtpAttempts(accountNumber);

        return otp;
    }

    private void incrementOtpAttempts(String accountNumber) {
        Cache cache = cacheManager.getCache("otpAttempts");
        Integer attempts = cache.get(accountNumber, Integer.class);
        if (attempts == null) {
            attempts = 1;
        } else {
            attempts++;
        }
        cache.put(accountNumber, attempts);
    }

    private void resetOtpAttempts(String accountNumber) {
        Cache cache = cacheManager.getCache("otpAttempts");
        cache.evict(accountNumber);
    }

    private int getOtpAttempts(String accountNumber) {
        Cache cache = cacheManager.getCache("otpAttempts");
        Integer attempts = cache.get(accountNumber, Integer.class);
        return attempts != null ? attempts : 0;
    }

    private String generateNewOTP(String accountNumber) {
        Random random = new Random();
        int otpValue = 100_000 + random.nextInt(900_000);
        String otp = String.valueOf(otpValue);

        // Save the new OTP information in the database
        OtpInfo otpInfo = new OtpInfo();
        otpInfo.setAccountNumber(accountNumber);
        otpInfo.setOtp(otp);
        otpInfo.setGeneratedAt(LocalDateTime.now());
        otpInfoRepository.save(otpInfo);
        return otp;
    }

    @Override
    public CompletableFuture<Boolean> sendOTPByEmail(String email, String name, String accountNumber, String otp) {
        // Compose the email content
        String subject = "OTP Verification";
        String emailText = emailService.getOtpLoginEmailTemplate(name, "xxx" + accountNumber.substring(3), otp);

        CompletableFuture<Void> emailSendingFuture = emailService.sendEmail(email, subject, emailText);

        return emailSendingFuture.thenApplyAsync(result -> true)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return false;
                });
    }

    @Override
    public boolean validateOTP(String accountNumber, String otp) {
        OtpInfo otpInfo = otpInfoRepository.findByAccountNumberAndOtp(accountNumber, otp);

        if (otpInfo != null) {
            // Check if OTP is not expired (5 minutes)

            if (isOtpExpired(otpInfo.getGeneratedAt())) {
                // OTP has expired, delete it from the database
                otpInfoRepository.delete(otpInfo);
                return false;
            } else {
                // Valid OTP, delete it from the database
                otpInfoRepository.delete(otpInfo);
                return true;
            }
        } else {
            // Invalid OTP or no OTP found
            throw new InvalidOtpException("Invalid OTP");
        }
    }

    private boolean isOtpExpired(LocalDateTime otpGeneratedAt) {
        LocalDateTime now = LocalDateTime.now();
        return otpGeneratedAt.isBefore(now.minusMinutes(OTP_EXPIRY_MINUTES));

    }
}
