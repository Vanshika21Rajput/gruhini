package com.example.Gruhani.Repositories;

import com.example.Gruhani.models.PasswordResetOtp;
import com.example.Gruhani.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {
    void deleteByUser(Users user);
    Optional<PasswordResetOtp> findByUserAndUsedFalseAndExpiryTimeAfter(
            Users user, LocalDateTime now
    );
}
