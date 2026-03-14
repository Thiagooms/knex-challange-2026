package com.knex.backend.services;

import com.knex.backend.dtos.transaction.TransactionCreateDto;
import com.knex.backend.dtos.transaction.TransactionResponseDto;
import com.knex.backend.entities.Product;
import com.knex.backend.entities.Transaction;
import com.knex.backend.entities.User;
import com.knex.backend.mappers.TransactionMapper;
import com.knex.backend.repositories.ProductRepository;
import com.knex.backend.repositories.TransactionRepository;
import com.knex.backend.repositories.UserRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    public TransactionService(
            TransactionRepository transactionRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            TransactionMapper transactionMapper
    ) {
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.transactionMapper = transactionMapper;
    }

    @Transactional
    public TransactionResponseDto createTransaction(TransactionCreateDto createDto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Product product = productRepository.findByIdAndNotDeleted(createDto.productId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!product.hasEnoughStock(createDto.quantity())) {
            throw new RuntimeException("Stock insuficiente para esta compra");
        }

        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(createDto.quantity()));

        try {
            product.decrementStock(createDto.quantity());
            productRepository.save(product);

            Transaction transaction = new Transaction(
                    createDto.quantity(),
                    totalPrice,
                    user,
                    product
            );

            Transaction savedTransaction = transactionRepository.save(transaction);
            return transactionMapper.toResponseDto(savedTransaction);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new RuntimeException("Falha ao processar compra: produto foi modificado. Tente novamente");
        }
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getUserTransactions(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return transactionRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(transactionMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TransactionResponseDto getTransactionById(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Você não tem permissão para ver esta transação");
        }

        return transactionMapper.toResponseDto(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getProductTransactions(Long productId) {
        Product product = productRepository.findByIdAndNotDeleted(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        return transactionRepository.findByProductOrderByCreatedAtDesc(product)
                .stream()
                .map(transactionMapper::toResponseDto)
                .toList();
    }
}
