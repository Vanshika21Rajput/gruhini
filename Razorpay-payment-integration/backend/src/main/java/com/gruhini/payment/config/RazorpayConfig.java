package com.gruhini.payment.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RazorpayConfig {
    
    @Value("${razorpay.key-id}")
    private String keyId;
    
    @Value("${razorpay.key-secret}")
    private String keySecret;
    
    @Bean
    public RazorpayClient razorpayClient() throws RazorpayException {
        // Log truncated key for debugging
        if (keyId != null && keyId.length() > 4) {
             log.info("Initializing Razorpay Client with Key ID ending in ...{}", keyId.substring(keyId.length() - 4));
        }
        return new RazorpayClient(keyId, keySecret);
    }
}
