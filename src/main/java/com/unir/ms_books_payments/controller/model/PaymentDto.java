package com.unir.ms_books_payments.controller.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PaymentDto {
    private Long product;

    private String description;

    private String orderId;

    private int userId;

    private int quantity;

    private String orderDate;

    private String deliveryStatus;
}
