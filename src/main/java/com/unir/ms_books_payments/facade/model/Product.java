package com.unir.ms_books_payments.facade.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Product {
    private Long isbn;
    private String title;
    private String author;
    private String genre;
    private String editorial;
    private String language;
    private Integer pages;
    private Integer year;
    private String description;
    private Double price = 0.0;
    private String image;
    private Date createAt;
    private boolean status;
    private Integer score;
}
