package com.unir.ms_books_payments.data;

import com.unir.ms_books_payments.data.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentsJpaRepository extends JpaRepository<Payment, Long> {

    // Método para filtrar los pagos por orderId
    List<Payment> findByOrderId(String orderId);
}
