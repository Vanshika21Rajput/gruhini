package com.gruhini.payment.repository;

import com.gruhini.payment.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, String> {
    List<AuditLog> findByIpAddressAndTimestampAfter(String ipAddress, LocalDateTime timestamp);
}
