package com.unir.ms_books_payments.external;

import com.unir.ms_books_payments.facade.model.Product;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Service
@Component
@Repository
@FeignClient(name = "ms-books-catalogue-service")
@Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
public interface ProductService {

    @GetMapping("v1/books/{id}")
    Product getProductId(@PathVariable("id") long  id);

}