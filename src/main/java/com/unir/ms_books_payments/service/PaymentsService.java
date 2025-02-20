package com.unir.ms_books_payments.service;

import com.unir.ms_books_payments.controller.model.PaymentDto;
import com.unir.ms_books_payments.controller.model.PaymentRequest;
import com.unir.ms_books_payments.data.model.Payment;

import java.util.List;

public interface PaymentsService {

    List<Payment> createPayment(List<PaymentRequest> request);

    Payment getPayment(String orderId);

    List<Payment> getPayments();

    List<Payment> getByOrderId(String orderId);

    boolean deleteOrder(String orderId);

    Payment updateOrderStatus(String orderId, String status);

    Payment updatePayment(String orderId, PaymentDto updateRequest);
}
