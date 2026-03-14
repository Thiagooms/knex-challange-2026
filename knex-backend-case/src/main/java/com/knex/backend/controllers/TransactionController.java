package com.knex.backend.controllers;

import com.knex.backend.dtos.transaction.TransactionCreateDto;
import com.knex.backend.dtos.transaction.TransactionResponseDto;
import com.knex.backend.services.TransactionService;
import com.knex.backend.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final SecurityUtils securityUtils;

    public TransactionController(TransactionService transactionService, SecurityUtils securityUtils) {
        this.transactionService = transactionService;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(
            @Valid @RequestBody TransactionCreateDto createDto
    ) {
        String userEmail = securityUtils.getAuthenticatedUserEmail();
        TransactionResponseDto transaction = transactionService.createTransaction(createDto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getUserTransactions() {
        String userEmail = securityUtils.getAuthenticatedUserEmail();
        List<TransactionResponseDto> transactions = transactionService.getUserTransactions(userEmail);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable Long id) {
        String userEmail = securityUtils.getAuthenticatedUserEmail();
        TransactionResponseDto transaction = transactionService.getTransactionById(id, userEmail);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<TransactionResponseDto>> getProductTransactions(@PathVariable Long productId) {
        List<TransactionResponseDto> transactions = transactionService.getProductTransactions(productId);
        return ResponseEntity.ok(transactions);
    }
}
