package com.unir.ms_books_payments.data.model;

import com.unir.ms_books_payments.controller.model.PaymentDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long product;

    private String description;

    private String orderId;

    private int userId;

    private int quantity;

    private Double price = 0.0;

    private String orderDate;

    private String deliveryStatus;

    public void update(PaymentDto paymentDto) {
        this.product = paymentDto.getProduct();
        this.description = paymentDto.getDescription();
        this.orderId = paymentDto.getOrderId();
        this.userId = paymentDto.getUserId();
        this.quantity = paymentDto.getQuantity();
        this.orderDate = paymentDto.getOrderDate();
        this.deliveryStatus = paymentDto.getDeliveryStatus();
    }
}
