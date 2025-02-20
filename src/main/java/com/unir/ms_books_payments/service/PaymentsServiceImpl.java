package com.unir.ms_books_payments.service;

import com.unir.ms_books_payments.controller.model.PaymentDto;
import com.unir.ms_books_payments.data.PaymentsJpaRepository;
import com.unir.ms_books_payments.data.model.Payment;
import com.unir.ms_books_payments.external.ProductService;
import com.unir.ms_books_payments.facade.model.Product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.unir.ms_books_payments.controller.model.PaymentRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class PaymentsServiceImpl implements PaymentsService {

    @Autowired //Inyeccion por campo (field injection). Es la menos recomendada.
    private PaymentsJpaRepository repository;

    @Autowired
    private ProductService productService;

    public PaymentsServiceImpl(PaymentsJpaRepository repository, ProductService productService) {
        this.repository = repository;
        this.productService = productService;
    }

    @Override
    public List<Payment> createPayment(List<PaymentRequest> _request) {

        List<Payment> payments = new ArrayList<>();
        for (PaymentRequest request : _request) {

            Product _product = productService.getProductId(request.getProducts());
            if (_product == null) {
                log.warn("Product with ID {} not found. Jump payment creation.", request.getProducts());
                // No agregar el pago si el producto no existe
            } else if (!_product.isStatus()) {
                log.warn("Product with ID {} found, but without stock. Jump payment creation.", request.getProducts());
            } else{
                Payment payment = new Payment();
                payment.setProduct(_product.getIsbn());
                payment.setDescription(_product.getTitle());
                payment.setPrice(_product.getPrice());
                payment.setOrderId(request.getOrderId());
                payment.setUserId(request.getUserId());
                payment.setQuantity(request.getQuantity());
                payment.setOrderDate(request.getOrderDate());
                payment.setDeliveryStatus(request.getDeliveryStatus());
                payments.add(payment);
            }
        }
        if (payments.isEmpty()) {
            log.warn("No payments were created because all products were invalid.");
            return Collections.emptyList(); // Retorna lista vacía si no se creó ningún pago
        }
        try
        {
            repository.saveAll(payments);
        } catch (Exception e) {
            return Collections.emptyList(); // Devuelve lista vacía si ocurre un error
        }
        return payments;
    }

    @Override
    public Payment getPayment(String id) {
        return repository.findById(Long.valueOf(id)).orElse(null);
    }

    @Override
    public List<Payment> getPayments() {
        List<Payment> payments = repository.findAll();
        return payments.isEmpty() ? null : payments;
    }

    @Override
    public List<Payment> getByOrderId(String orderId) {
        List<Payment> payments = repository.findByOrderId(orderId);
        return payments.isEmpty() ? null : payments;
    }
    @Override
    public boolean deleteOrder(String orderId) {
        try {
            List<Payment> payments = repository.findByOrderId(orderId);
            if (payments.isEmpty()) {
                return false;
            }
            repository.deleteAll(payments);
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar las órdenes: " + e.getMessage());
            return false;
        }
    }
    @Override
    public Payment updateOrderStatus(String orderId, String status) {
        Payment payment = (Payment) repository.findByOrderId(orderId);
        if (payment == null) {
            log.warn("Payment with orderId {} not found.", status);
            return null;
        }else{
            payment.setDeliveryStatus(status);
            repository.save(payment);
            return payment;
        }
    }

    @Override
    public Payment updatePayment(String orderId, PaymentDto updateRequest) {
        Payment payment = (Payment) repository.findByOrderId(orderId);
        if (payment != null) {
            payment.update(updateRequest);
            repository.save(payment);
            return payment;
        } else {
            return null;
        }
    }
}

