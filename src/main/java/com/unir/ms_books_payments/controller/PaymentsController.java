package com.unir.ms_books_payments.controller;

import com.unir.ms_books_payments.controller.model.PaymentDto;
import com.unir.ms_books_payments.controller.model.PaymentRequest;
import com.unir.ms_books_payments.data.model.Payment;
import com.unir.ms_books_payments.service.PaymentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payments Controller", description = "Microservicio encargado de exponer operaciones CRUD sobre ordenes alojados en una base de datos relacional..")
public class PaymentsController {

    private final PaymentsService service; //Inyeccion por constructor mediante @RequiredArgsConstructor. Y, también es inyección por interfaz.

    @PostMapping("/payments")
    @Operation(
            operationId = "Insertar un orden",
            description = "Operación de escritura",
            summary = "Se crea una orden partir de sus datos.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la orden a crear.",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentRequest.class))))
    @ApiResponse(
            responseCode = "201",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Payment.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Datos incorrectos introducidos.")
    public ResponseEntity<List<Payment>> createPayment(@RequestBody @Valid List<PaymentRequest> request) { //Se valida con Jakarta Validation API

        log.info("Creating order...");
        List<Payment> created = service.createPayment(request);

        if (created != null && !created.isEmpty()) {
            log.info("Orden creada exitosamente: {}", created);
            return ResponseEntity.status(201).body(created);
        } else {
            log.warn("No se pudo crear la orden. Datos inválidos proporcionados.");
            return ResponseEntity.status(400).body(Collections.emptyList());
        }
    }

    @GetMapping("/payments")
    @Operation(
            operationId = "Obtener las ordenes",
            description = "Operación de lectura",
            summary = "Se devuelven todas las ordenes.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Payment.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se han encontrado las ordenes del sistema.")
    public ResponseEntity<List<Payment>> getPayments() {
        List<Payment> payments = service.getPayments();
        if(payments != null) {
            return ResponseEntity.ok(payments);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/payments/{orderId}")
    @Operation(
            operationId = "Obtener un orden",
            description = "Operacion de lectura",
            summary = "Se devuelve un orden a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Payment.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado la orden con el identificador indicado.")
    public ResponseEntity<List<Payment>> getPayment(@PathVariable String orderId) {
        List<Payment> payments = service.getByOrderId(orderId);
        if(payments != null) {
            return ResponseEntity.ok(payments);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PatchMapping("/payments/{orderId}")
    @Operation(
            operationId = "Actualizar estado de la orden",
            description = "Permite actualizar el estado de una orden existente.",
            summary = "Actualiza el estado de la orden a partir de su identificador.")
    public ResponseEntity<Payment> updateOrderStatus(@PathVariable String orderId, @RequestParam String deliveryStatus) {
        log.info("Actualizando estado de la orden: {} a estado: {}", orderId, deliveryStatus);
        Payment updated = service.updateOrderStatus(orderId, deliveryStatus);

        if (updated != null) {
            log.info("Estado de la orden actualizado exitosamente: {}", updated);
            return ResponseEntity.ok(updated);
        } else {
            log.warn("No se encontró la orden con el ID {}.", orderId);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/payments/{orderId}")
    @Operation(
            operationId = "Borrar una orden",
            description = "Elimina una orden por su identificador.",
            summary = "Se elimina una orden a partir de su identificador.")
    public ResponseEntity<?> deleteOrder(@PathVariable String orderId) {
        log.info("Eliminando orden con ID: {}", orderId);
        boolean deleted = service.deleteOrder(orderId);
        if (deleted) {
            log.info("Orden con ID {} eliminada exitosamente.", orderId);
            return ResponseEntity.ok("Orden eliminada exitosamente.");
        } else {
            log.warn("Orden con ID {} no encontrada.", orderId);
            return ResponseEntity.status(404).body("No se encontró la orden con el ID proporcionado.");
        }
    }

    @PutMapping("/payments/{orderId}")
    @Operation(
            operationId = "Modificar totalmente un Payment",
            description = "Operación de escritura",
            summary = "Se modifica totalmente un Payment.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del Payment a actualizar.",
                    required = true,
                    content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = PaymentDto.class))))
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Payment.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Payment no encontrado.")
    public ResponseEntity<Payment> updatePayment(@PathVariable String orderId, @RequestBody PaymentDto body) {

        Payment updated = service.updatePayment(orderId, body);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
