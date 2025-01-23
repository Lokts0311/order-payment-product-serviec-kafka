package com.example.demo.repository;

import com.example.demo.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "select * from payment p where p.session_id = :sessionId",nativeQuery = true)
    Optional<Payment> findBySessionId(@Param("sessionId") String sessionId);
}
